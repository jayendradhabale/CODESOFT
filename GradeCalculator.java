import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GradeCalculator {

    static final int[][] THRESHOLDS = {
        {90, 100},
        {80,  89},
        {70,  79},
        {60,  69},
        {50,  59},
        {40,  49},
        { 0,  39},
    };
    static final String[] GRADES  = {"A+", "A",  "B+", "B",  "C",  "D",  "F"};
    static final double[] GPA_PTS = {10.0, 9.0,  8.0,  7.0,  6.0,  5.0,  0.0};
    static final String[] REMARKS = {
        "Outstanding", "Excellent", "Very Good", "Good",
        "Average", "Below Average", "Fail"
    };

    static int gradeIndex(double avg) {
        for (int i = 0; i < THRESHOLDS.length; i++) {
            if (avg >= THRESHOLDS[i][0] && avg <= THRESHOLDS[i][1]) return i;
        }
        return GRADES.length - 1;
    }

    static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= min && v <= max) return v;
                System.out.printf("  ⚠  Enter a value between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠  Please enter a valid integer.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println();
        System.out.println("  ╔════════════════════════════════════════╗");
        System.out.println("  ║     STUDENT GRADE CALCULATOR  v1.0    ║");
        System.out.println("  ╚════════════════════════════════════════╝");
        System.out.println();

        System.out.print("  Student Name : ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) name = "Student";

        int numSubjects = readInt(sc, "  No. of Subjects (1-10) : ", 1, 10);

        List<String> subjects = new ArrayList<>();
        List<Integer> marks   = new ArrayList<>();

        System.out.println();
        for (int i = 1; i <= numSubjects; i++) {
            System.out.printf("  Subject %d Name : ", i);
            String sub = sc.nextLine().trim();
            if (sub.isEmpty()) sub = "Subject " + i;
            subjects.add(sub);

            int m = readInt(sc, "  Marks (0-100)  : ", 0, 100);
            marks.add(m);
            System.out.println();
        }

        int    totalMarks    = marks.stream().mapToInt(Integer::intValue).sum();
        int    totalPossible = numSubjects * 100;
        double avgPercent    = (double) totalMarks / numSubjects;
        int    gi            = gradeIndex(avgPercent);
        String grade         = GRADES[gi];
        double gpa           = GPA_PTS[gi];
        String remark        = REMARKS[gi];
        boolean passed       = avgPercent >= 40;

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║              S T U D E N T   R E P O R T        ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.printf("  Student  : %s%n%n", name);

        System.out.printf("  %-22s %-8s %-5s%n", "Subject", "Marks", "Grade");
        System.out.println("  " + "-".repeat(38));
        for (int i = 0; i < numSubjects; i++) {
            double m   = marks.get(i);
            int    sgi = gradeIndex(m);
            System.out.printf("  %-22s %-8s %-5s%n",
                    subjects.get(i), (int) m + " / 100", GRADES[sgi]);
        }
        System.out.println("  " + "-".repeat(38));

        System.out.printf("  %-22s %d / %d%n",  "Total Marks",  totalMarks, totalPossible);
        System.out.printf("  %-22s %.2f %%%n",   "Average",      avgPercent);
        System.out.printf("  %-22s %s%n",        "Grade",        grade);
        System.out.printf("  %-22s %.1f / 10.0%n","GPA",         gpa);
        System.out.printf("  %-22s %s%n",        "Remarks",      remark);
        System.out.printf("  %-22s %s%n",        "Result",       passed ? "PASS" : "FAIL");
        System.out.println();

        System.out.println("  Grade Scale:");
        System.out.println("  90-100 -> A+ (10.0)   80-89 -> A  (9.0)   70-79 -> B+ (8.0)");
        System.out.println("  60-69  -> B  (7.0)    50-59 -> C  (6.0)   40-49 -> D  (5.0)   0-39 -> F (0.0)");
        System.out.println();
        System.out.println("  ==================================================");

        sc.close();
    }
}
