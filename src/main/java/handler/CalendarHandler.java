package handler;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import snippets.*;

/**
 * This handler can read Google Calendar Events if credentials was set.<br>
 * Otherwise it can only output today's info.
 * 
 * @see Handler
 * 
 * @author <a href="mailto:asas1asas200@gmaill.com">Zeng</a>
 */
public class CalendarHandler extends Handler {
	private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	/**
	 * Global instance of the scopes required by this quickstart. If modifying these
	 * scopes, delete your previously saved tokens/ folder.
	 */
	private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = CalendarHandler.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	private List<String> getCalendarInfo() throws IOException, GeneralSecurityException {
		List<String> activities = new ArrayList<String>();
		// Build a new authorized API client service.
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();

		// List the next 10 events from the primary calendar.
		DateTime now = new DateTime(System.currentTimeMillis());
		Events events = service.events().list("primary").setMaxResults(10).setTimeMin(now).setOrderBy("startTime")
				.setSingleEvents(true).execute();
		List<Event> items = events.getItems();
		if (items.isEmpty()) {
			activities.add("No upcoming events found.");
			// System.out.println("No upcoming events found.");
		} else {
			// activities.add("Upcoming events\n");
			// System.out.println("Upcoming events");
			for (Event event : items) {
				DateTime start = event.getStart().getDateTime();
				if (start == null) {
					start = event.getStart().getDate();
				}

				// System.out.printf("%s (%s)\n", event.getSummary(), start);
				activities.add(String.format("%s (%s)", event.getSummary(), start));
			}
		}
		return activities;
	}

	private LocalDateTime today;

	/**
	 * Initialize CalendarHandler and set ifOutput as {@code true}
	 */
	public CalendarHandler() {
		ifOutput = true;
		today = LocalDateTime.now();
	}

	@Override
	protected void readConfig(String fileName) {
	}

	//@formatter:off
	/** render example
	 *    __     __  __     __   __       |              xeee             |An error occurred when load calendar info:
	 *   /\ \   /\ \/\ \   /\ "-.\ \      |             d888R             |  java.io.FileNotFoundException:
	 *  _\_\ \  \ \ \_\ \  \ \ \-.  \     |            d8888R             |  Resource not found: /credentials.json
	 * /\_____\  \ \_____\  \ \_\\"\_\    |           @ 8888R             |
	 * \/_____/   \/_____/   \/_/ \/_/    |         .P  8888R             |
	 * ___________________________________|        :F   8888R             |
	 *            8888 888b. 888          |       x"    8888R             |
	 *            8www 8  .8  8           |      d8eeeee88888eer          |
	 *            8    8wwK'  8           |             8888R             |
	 *            8    8  Yb 888          |             8888R             |
	 *                                    |          "*%%%%%%**~          |
	 */
	//@formatter:on
	private String render(List<String> monthImage, List<String> dayImage, List<String> dayOfWeekImage,
			List<String> activities, String dayColor) {

		StringBuilder output = new StringBuilder();

		// Width: 11
		final String weekdayLayoutOffset = "           ";

		// Width: 35
		final String blockLayoutOffset = "                                   ";

		// Width: 35
		// final String horizontalBorder = "-----------------------------------";
		final String horizontalBorder = "___________________________________";

		for (int i = 0; i < 11; i++) {
			if (i < 5) { // monthImage.size() is 5
				output.append(ANSIColor.GREEN.toString());
				output.append(monthImage.get(i));
				output.append(ANSIColor.RESET.toString());
			} else if (i == 5) {
				output.append(horizontalBorder);
			} else if (i < 10) { // dayOfWeekImage.size() is 4
				output.append(weekdayLayoutOffset);
				output.append(dayColor);
				output.append(dayOfWeekImage.get(i - 6));
				output.append(ANSIColor.RESET.toString());
			} else {
				output.append(blockLayoutOffset);
			}
			output.append('|');

			output.append(dayColor);
			output.append(dayImage.get(i));
			output.append(ANSIColor.RESET.toString());

			output.append('|');

			if (i < activities.size())
				output.append(activities.get(i));
			output.append('\n');
		}

		return output.toString();
	}

	@Override
	public String toString() {
		// Height: 5
		List<String> monthImage = CalendarImage.getMonth(today.getMonthValue());

		// Height: 11
		List<String> dayImage = CalendarImage.getDay(today.getDayOfMonth());

		// Height: 4
		List<String> dayOfWeekImage = CalendarImage.getDayOfWeek(today.getDayOfWeek().toString());

		List<String> activities;
		try {
			activities = getCalendarInfo();
		} catch (GeneralSecurityException | IOException e) {
			activities = new ArrayList<String>();
			// activities.add(String.format("An error occurred when load calendar info:
			// \n%s", e));
			activities.add(String.format("An error occurred when load calendar info: "));

			activities.add(String.format("\t%s:", e.getClass().getCanonicalName()));
			activities.add(String.format("\t%s", e.getMessage()));
		}

		String dayColor;
		switch (today.getDayOfWeek().toString()) {
			case "SUNDAY":
			case "SATURDAY":
				dayColor = ANSIColor.RED.toString();
				break;
			default:
				dayColor = ANSIColor.CYAN.toString();
		}

		return render(monthImage, dayImage, dayOfWeekImage, activities, dayColor);
	}
}
