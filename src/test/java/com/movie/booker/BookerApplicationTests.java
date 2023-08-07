package com.movie.booker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.movie.booker.dto.Message;
import com.movie.booker.dto.Movie;
import com.movie.booker.dto.MovieRequest;
import com.movie.booker.dto.Ticket;
import com.movie.booker.repository.MovieRepository;
import com.movie.booker.repository.TicketRepository;
import com.movie.booker.service.MovieService;

@SpringBootTest
class BookerApplicationTests {

	@Mock
	private MovieRepository repository;

	@Mock
	private GridFsOperations gridFsOperations;

	@Mock
	private MongoTemplate mongoTemplate;

	@InjectMocks
	private MovieService movieService;

	@Mock
	private TicketRepository ticketRepository;

	@Mock
	private KafkaTemplate<String, String> kafkaTemplate;

	@Test
	public void testSearchMovie() {
		String searchName = "Movie 1";
		List<Movie> mockMovies = new ArrayList<>();
		mockMovies.add(new Movie("1", "Movie 1", "Description 1", null,
				"2h", "Action", "PG-13", "English", "2023-08-04",
				"2D", "Location 1", new ArrayList<>(), "Available", 100));
		Query query = new Query(Criteria.where("title").regex(searchName, "i"));
		when(mongoTemplate.find(query, Movie.class)).thenReturn(mockMovies);
		List<Movie> result = movieService.searchMovie(searchName);
		verify(mongoTemplate, times(1)).find(query, Movie.class);
		assertEquals(1, result.size());
		assertEquals("Movie 1", result.get(0).getTitle());
		assertEquals("Description 1", result.get(0).getDescription());
	}

	@Test
	public void testGetSearchSuggestions() {
		String searchTerm = "Movie";
		List<Movie> mockMovies = new ArrayList<>();
		mockMovies.add(new Movie("1", "Movie 1", "Description 1", null,
				"2h", "Action", "PG-13", "English", "2023-08-04",
				"2D", "Location 1", new ArrayList<>(), "Available", 100));
		mockMovies.add(new Movie("2", "Movie 2", "Description 2", null,
				"2h", "Drama", "PG-13", "English", "2023-08-05",
				"2D", "Location 2", new ArrayList<>(), "Available", 150));
		Query query = new Query(Criteria.where("title").regex(searchTerm, "i")).limit(10);
		when(mongoTemplate.find(query, Movie.class)).thenReturn(mockMovies);
		Set<String> result = movieService.getSearchSuggestions(searchTerm);
		verify(mongoTemplate, times(1)).find(query, Movie.class);
		Set<String> expectedSuggestions = new HashSet<>();
		expectedSuggestions.add("Movie 1");
		expectedSuggestions.add("Movie 2");
		assertEquals(expectedSuggestions, result);
	}

	@Test
	public void testAddMovie() throws IOException {
		MovieRequest movieRequest = new MovieRequest();
		movieRequest.setTitle("Mission Impossible: Dead Reckoning - Part One");
		movieRequest.setDescription("In Mission: Impossible - Dead Reckoning Part One, Ethan Hunt...");
		movieRequest.setDuration("2h 45m");
		movieRequest.setGenre("Action, Adventure, Thriller");
		movieRequest.setMovieRating("UA");
		movieRequest.setLanguage("English, Hindi, Tamil");
		movieRequest.setReleaseDate("12 Jul, 2023");
		movieRequest.setViewType("2D, ICE 3D, MX4D 3D, 4DX 3D, IMAX 3D, 3D");
		movieRequest.setLocation("Chennai");
		List<List<String>> theatres = new ArrayList<>();
		theatres.add(List.of("Cinemass", "75"));
		theatres.add(List.of("Cinemax", "100"));
		movieRequest.setTheatres(theatres);
		MultipartFile mockFile = new MockMultipartFile("test.png", new byte[0]);
		when(repository.save(any(Movie.class))).thenReturn(new Movie());
		when(gridFsOperations.store(any(), anyString())).thenReturn(new ObjectId());
		Message result = movieService.addMovie(movieRequest, mockFile);
		verify(repository, times(1)).save(any(Movie.class));
		assertEquals("Movie added successfully!", result.getMessage());
	}

	@Test
	public void testGetMovies() {
		int page = 0;
		int size = 10;
		List<Movie> mockMovies = new ArrayList<>();
		mockMovies.add(new Movie("1", "Movie 1", "Description 1", null,
				"2h", "Action", "PG-13", "English", "2023-08-04",
				"2D", "Location 1", new ArrayList<>(), "Available", 100));
		when(mongoTemplate.find(any(Query.class), eq(Movie.class))).thenReturn(mockMovies);
		when(mongoTemplate.count(any(Query.class), eq(Movie.class))).thenReturn((long) mockMovies.size());
		List<Movie> result = movieService.getMovies(page, size);
		verify(mongoTemplate, times(1)).find(any(Query.class), eq(Movie.class));
		assertEquals(1, result.size());
		assertEquals("Movie 1", result.get(0).getTitle());
	}

	@Test
	public void testDeleteMovie() {
		String movieId = "1";
		Message result = movieService.deleteMovie(movieId);
		verify(repository, times(1)).deleteById(movieId);
		assertEquals("Movie deleted successfully!", result.getMessage());
	}

	@Test
    public void testBookTicket() {
        Ticket ticketDetails = new Ticket();
        ticketDetails.setMovieId("1");
        ticketDetails.setUsername("john_doe");
        ticketDetails.setTheatreName("Cinemass");
        ticketDetails.setNumberOfTickets(2);
        Movie movieFromDB = new Movie();
        movieFromDB.setId("1");
        movieFromDB.setTitle("Mission Impossible: Dead Reckoning - Part One");
        movieFromDB.setTheatres(Arrays.asList(Arrays.asList("Cinemass", "75")));
		movieFromDB.setTickets(2);
        when(repository.findById(ticketDetails.getMovieId())).thenReturn(Optional.of(movieFromDB));
        when(repository.save(any(Movie.class))).thenReturn(movieFromDB);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());
        Message result = movieService.bookTicket(ticketDetails);
        verify(repository, times(1)).findById(ticketDetails.getMovieId());
        verify(repository, times(1)).save(any(Movie.class));
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(kafkaTemplate, times(1)).send(eq("bookie-events"), anyString());
        assertEquals("Tickets booked successfully!", result.getMessage());
    }

}
