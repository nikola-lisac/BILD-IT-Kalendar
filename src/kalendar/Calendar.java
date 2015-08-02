package kalendar;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Calendar {

	private static String[] months = { " ", "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };

	int numberOfMonth = 0;
	int year = 0;

	Calendar() {

	}

	Calendar(int numberOfMonth, int year) {
		this.numberOfMonth = numberOfMonth;
		this.year = year;
	}

	/*
	 * Metoda trazi od korisnika da unese mjesec i godinu i pravi instancu klase
	 * Calendar kojoj dodjeljuje vrijednosti mjeseca i godine kao data fields
	 */
	public static void showTheMonthsCalendar() {
		Scanner in = null;
		int userMonth = 1;
		int userYear = 2015;
		try {
			in = new Scanner(System.in);

			// korisnik unosi mjesec
			System.out
					.print("Unesite broj mjeseca (1 - Januar, 2 - Februar, ... 12 - Decembar): ");
			userMonth = in.nextInt();

			// korisnik unosi godinu
			System.out.print("Unesite broj godine: ");
			userYear = in.nextInt();

		} catch (InputMismatchException a) {
			System.out.println("Pogresan unos.");
			showTheMonthsCalendar();
		}

		// vracamo instancu klase sa unesenim data fieldsima
		Calendar calendar = new Calendar(userMonth, userYear);
		System.out.println();

		// stampanje zaglavlja kalendara za dati mjesec i godinu
		calendar.printCalendarHeader();

		// stampanje kalendara za dati mjesec i godinu
		calendar.printMonth();

		// stampanje podsjetnika za dati mjesec i godinu
		Reminder.printReminders(Reminder.checkForReminder(userMonth, userYear));
		System.out.println();
		calendar.showCalendarMenu();
	}

	/*
	 * Korisnicki meni prilikom prikaza kalendara
	 */
	public void showCalendarMenu() {
		System.out.println("Izaberite jednu od opcija: ");
		System.out.println("[1] Unos podsjetnika za ovaj mjesec");
		System.out.println("[2] Povratak na korisnicki meni");

		Scanner input = null;
		int choice = 0;
		try {
			input = new Scanner(System.in);
			choice = input.nextInt();

		} catch (InputMismatchException e) {
			System.out.println("Pogresan unos.");
			this.showCalendarMenu();
		}

		switch (choice) {

		case 1: { // novi podsjetnik
			Reminder.newReminder(this);
			Reminder.printRemindersToFile();
			break;
		}
		case 2: { // povratak u korisnicki meni
			userMenu();
			break;
		}
		default: {// nepostojeca opcija, ponovno pozivanje metode
			System.out.println("Nepostojeca opcija.");
			this.showCalendarMenu();
		}
		}

	}

	/*
	 * Method print calendar header which includes name of the month, year and
	 * names of days in a week
	 */
	void printCalendarHeader() {

		System.out.println("         " + getMonths(this.numberOfMonth) + "  "
				+ this.year + "       ");
		System.out.println("------------------------------------");
		System.out.println(" Sun  Mon  Tue  Wed  Thu  Fri  Sat");
	}

	void printMonth() {

		int startDay = this.getFirstDayOfTheMonth();// prvi dan u mjesecu
		int numberOfDays = this.numOfDaysInAMonth();// broj dana u mjesecu
		/*
		 * print spaces before the days if first day od the month is wednessday
		 * you need to print 4 spaces twice so it will be aligned
		 */
		for (int space = 0; space < startDay; space++) {
			System.out.print("     ");
		}
		int countDaysPerLine = startDay;// counter starts at the number of the
										// first day because printed spaces
										// count in placement of the number of
										// days
		for (int i = 1; i <= numberOfDays; i++) {
			countDaysPerLine++;
			if (countDaysPerLine % 7 == 0) {// ako je je ispisao 7 po liniji
				if (i < 10) {// ako je broj jednocifren
					System.out.println("  " + i + "  ");// ispisuje u novoj
														// liniji broj sa dva
														// razmaka ispred i dva
														// razmaka iza
				} else {// ako je dvocifren
					System.out.println(" " + i + "  ");// ispisuje ga u novoj
														// liniji sa jednim
														// razmakom ispred i dva
														// razmaka iza
				}
				countDaysPerLine = 0;// vracamo brojac na nulu
			} else {// ako nije ispisao 7 u liniji
				if (i < 10) {// ako je jednocifren dan
					System.out.print("  " + i + "  ");// ispisuje ga u istoj
														// liniji sa jednim
														// razmakom ispred i dva
														// razmaka iza
				} else {// ako je dvocifren
					System.out.print(" " + i + "  ");// ispisuje ga sa 2 razmaka
														// iza
				}
			}
		}

	}

	/*
	 * method return number of days for the month
	 */
	int numOfDaysInAMonth() {
		if (this.numberOfMonth == 4 || this.numberOfMonth == 6
				|| this.numberOfMonth == 9 || this.numberOfMonth == 11) {
			return 30;
		}

		// drugi mjesec godine koja nije prestupna ima 28 dana
		else if (this.numberOfMonth == 2 && !this.isLeap()) {
			return 28;
		}

		// drugi mjesec prestupne godine ima 29 dana
		else if (this.numberOfMonth == 2 && this.isLeap()) {
			return 29;

		}

		// ostali mjeseci imaju 31 dan
		else {
			return 31;
		}
	}

	/*
	 * method to determent is the year leap or not returns true if it is leap
	 * returns false if it isn't
	 */
	boolean isLeap() {
		if (this.year % 4 == 0
				&& (this.year % 100 != 0 || this.year % 400 == 0)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Method calculates and returns what day of the week is the 1st day of the
	 * entered month of entered year Method is based on Zeller's algorithm (see:
	 * https://en.wikipedia.org/wiki/Zeller%27s_congruence) Zeller's formula is:
	 * dayOfTheWeek = ((q + (((m + 1) * 26) / 10) + k + (k / 4) + (j / 4)) + (5
	 * * j)) % 7 where: h is the day of the week (0 = Saturday, 1 = Sunday, 2 =
	 * Monday, ..., 6 = Friday) q is the day of the month (I needed just the
	 * first day so q = 1) m is the month (3 = March, 4 = April, 5 = May, ...,
	 * 14 = February) K the year of the century (year % 100). J is the
	 * zero-based century (the zero-based centuries for 1995 and 2000 are 19 and
	 * 20 respectively) My days of week are numbered as 0-Sunday,
	 * 1-Monday...6-Saturday so I had to ajust values of the result int he last
	 * if-else block
	 */
	int getFirstDayOfTheMonth() {
		int m = this.numberOfMonth;
		int y = this.year;

		if (m < 3) {
			m += 12;
			y -= 1;
		}

		int k = y % 100;
		int j = y / 100;

		/*
		 *  Zeller-ova formula (jedinica na pocetku znaci da trazim dan u sedmici za prvi dan mjeseca
		 */
		int day = ((1 + (((m + 1) * 26) / 10) + k + (k / 4) + (j / 4)) + (5 * j)) % 7;

		if (day == 0) {
			day = 6;
		} else {
			day -= 1;
		}
		return day;
	}

	/*
	 * Method return name of the month based on its number
	 */
	static String getMonths(int numberOfMonth) {
		return months[numberOfMonth];
	}

	/*
	 * method prints app headline
	 */
	public static void appHeadline() {
		Reminder.enterRemindersFromFile();
		System.out.println("\t   KALENDAR");
		System.out.println("*********************************");
	}

	/*
	 * Korisnicki meni
	 */
	public static void userMenu() {
		System.out
				.println("Izaberite zeljenu opciju unosom odgovarajuceg broja:\n");
		System.out.println("[1] Kalendar za odredjeni mjesec");
		System.out.println("[2] Pregled podsjetnika\n");
		System.out.println("[0] Izlaz iz programa");

		Scanner input = null;
		int userChoice = 0;// korisnikov izbor
		try {
			input = new Scanner(System.in);
			userChoice = input.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Pogresan unos.");
			userMenu();
		}

		switch (userChoice) {
		case 0: { // izlaz iz programa
			System.out.println("Prijatan dan!");
			System.exit(0);
			break;
		}

		case 1: {
			showTheMonthsCalendar();
			break;
		}
		case 2: {
			System.out.println("Postojeci podsjetnici: ");
			Reminder.printAllReminders();
			userMenu();
			break;
		}

		}
	}

}
