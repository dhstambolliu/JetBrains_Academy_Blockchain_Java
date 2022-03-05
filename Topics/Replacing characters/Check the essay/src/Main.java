import java.util.Scanner;


class CheckTheEssay {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        System.out.println(text.replaceAll("Franse", "France")
                .replaceAll("Eifel t", "Eiffel T")
                .replaceAll("19th", "XIXth").replaceAll("20th", "XXth")
                .replaceAll("21st", "XXIst"));

    }
}