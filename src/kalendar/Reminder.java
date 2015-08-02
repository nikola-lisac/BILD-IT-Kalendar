package kalendar;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Reminder extends Calendar {
	String dateOfReminder = "";
	String reminderNote = " ";
	static ArrayList<Reminder> reminderList = new ArrayList<>(); // lista koja
																	// zadrzi
																	// sve
																	// podsjetnike

	Reminder() {

	}

	Reminder(String date, String note) {
		dateOfReminder = date;
		reminderNote = note;
	}

	/*
	 * konstruktor za podsjetnik day, month,year se smjestaju u string
	 * dateOfReminder a note u reminderNote
	 */
	Reminder(int day, int month, int year, String note) {
		if (day < 10) {
			dateOfReminder = "0" + Integer.toString(day) + ":";
		} else {
			dateOfReminder = Integer.toString(day) + ":";
		}
		if (month < 10) {
			dateOfReminder += "0" + Integer.toString(month) + ":";
		} else {
			dateOfReminder += Integer.toString(month) + ":";
		}
		dateOfReminder += Integer.toString(year) + ":";
		reminderNote = note;

	}

	/*
	 * stampanje podsjetnika iz liste u fajl
	 */
	public static void printRemindersToFile() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream("reminders.txt"));
			/*
			 * petlja prolazi sve podsjetnike u i za svaki upisuje datum
			 * podsjetnika i tekst podsjetnika, odvojene sa ; u fajl
			 */
			for (Reminder rem : reminderList) {

				writer.write(rem.dateOfReminder + ";" + rem.reminderNote + "\n");
			}
		} catch (Exception el) {
			System.out.println(el);
		} finally {
			writer.close();
		}
	}

	/*
	 * unosenje podsjetnika iz fajla
	 */
	static void enterRemindersFromFile() {
		ArrayList<Reminder> tempReminderList = new ArrayList<>();// privremena
																	// lista
																	// podsjetnika
		BufferedReader output = null;
		try {
			output = new BufferedReader(new FileReader("reminders.txt"));
			String line = "";
			while ((line = output.readLine()) != null) {
				String[] lineArray = line.split(";");// niz sacinjen vrijednosti
														// dateOfReminder i
														// reminderNote
				Reminder rem = new Reminder(lineArray[0], lineArray[1]);

				tempReminderList.add(rem);// dodavanje novog remindera u
											// privremenu listu
			}
			reminderList = tempReminderList;
		} catch (Exception e) {
			System.out.println();
		} finally {

			try {
				output.close();
			} catch (IOException exc) {
				System.out.println("Nema fajla!");
				System.exit(0);
			}
		}
	}

	/*
	 * Meni za kreiranje novog podsjetnika
	 */
	public static void newReminder(Calendar cal) {
		Scanner input = null;
		int numOfDay = 0;
		String reminder = "";
		try {
			input = new Scanner(System.in);
			System.out
					.print("Unesite broj dan za koji zelite staviti podsjetnik: ");
			numOfDay = input.nextInt();// dan za podsjetnik
			input.nextLine();
			// ako je uneseni dan 0 ili veci od broja dana u tom mjeseci ponovo
			// trazi da unese dan
			if (numOfDay < 1 && numOfDay > cal.numOfDaysInAMonth()) {
				System.out
						.println("Unijeli ste nepostojeci dan, pokusajte ponovo.");
				newReminder(cal);
			} else {
				System.out.print("Unesite tekst podsjetnika: ");
				reminder = input.nextLine();
			}
		} catch (InputMismatchException e) {
			System.out.println("Pogresan unos.");
			newReminder(cal);
		}

		// kreiranje novog podsjetnika
		Reminder rem = new Reminder(numOfDay, cal.numberOfMonth, cal.year,
				reminder);
		// dodavanje novog podsjetnika u listu
		reminderList.add(rem);
		printRemindersToFile();
		System.out.println("Podsjetnik je unesen.");
		userMenu();

	}

	/*
	 * printanje svih podsjetnika
	 */
	static void printAllReminders() {

		System.out.println("Lista podsjetnika");
		System.out.println("---------------------");

		for (Reminder rem : reminderList) {
			String date = rem.dateOfReminder.replace(':', '.');
			System.out.println(date + " " + rem.reminderNote + "\n");
		}
	}

	/*
	 * Metoda koja pronalazi remindere za dati mjesec i smjesta ih u listu, a
	 * zatim prosledjuje tu listu
	 */
	static ArrayList<Reminder> checkForReminder(int month, int year) {
		ArrayList<Reminder> remindersForPrinting = new ArrayList<>();
		if (reminderList.size() > 0) {
			for (Reminder r : reminderList) {
				String[] date = r.dateOfReminder.split(":");
				int monthReminder = Integer.parseInt(date[1]);
				int yearReminder = Integer.parseInt(date[2]);
				if (month == monthReminder && year == yearReminder) {
					remindersForPrinting.add(r);
				}

			}
		}
		return remindersForPrinting;

	}

	/*
	 * Metoda koja printa remindere iz liste za printanje
	 */
	static void printReminders(ArrayList<Reminder> remindersForPrinting) {
		System.out.println("\n");
		if (remindersForPrinting.size() > 0) {
			for (Reminder reminder : remindersForPrinting) {
				String date = reminder.dateOfReminder.replace(':', '.');

				System.out.println(date + " " + reminder.reminderNote);
			}
		}
	}

}
