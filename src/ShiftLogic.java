import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class ShiftLogic {
    ArrayList<String> list = new ArrayList<>();
    final double TIMELONN = 166.46;
    final double EXTRA1 = 110;
    final double EXTRA2 = 55;

    public ArrayList<String> getShiftList() {
        return list;
    }

    // Method to add a shift detail
    public void addShift(String shiftDetail) {
        list.add(shiftDetail);
    }

    // Method to calculate total pay
    public double calculateTotalPay() {
        double total = 0;
        for (String s : list) {
            total += getVaktLonn(s);
        }
        return total;
    }

    public double getVaktLonn(String shiftString) {
        Shift shift = parseShiftString(shiftString);
        double diffTotalRounded = calculateTimeDifference(shift);
        return calculatePay(shift.dayOfWeek, diffTotalRounded, shift.startHour, shift.endHour);
    }

    private Shift parseShiftString(String s) {
        String[] parts = s.split("-");
        return new Shift(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4])
        );
    }

    private double calculateTimeDifference(Shift shift) {
        Calendar start = Calendar.getInstance();
        start.set(shift.year, shift.month, shift.day, shift.startHour, 0);
        Calendar end = Calendar.getInstance();
        end.set(shift.year, shift.month, shift.day, shift.endHour, 0);
        long diff = end.getTimeInMillis() - start.getTimeInMillis();
        return Math.round((diff / (60.0 * 60.0 * 1000.0)) * 100.0) / 100.0;
    }

    private double calculatePay(DayOfWeek dayOfWeek, double diffTotalRounded, int startHour, int endHour) {
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            return calculateSaturdayPay(startHour, endHour);
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            return calculateSundayPay(startHour, endHour);
        } else {
            return calculateWeekdayPay(diffTotalRounded);
        }
    }

    private double calculateSaturdayPay(int startHour, int endHour) {
        double total = 0;
        for (int i = startHour; i <= endHour; i++) {
            total += getSaturdayHourlyRate(i);
        }
        return adjustTotal(total);
    }

    private double getSaturdayHourlyRate(int hour) {
        if (hour < 15) return TIMELONN;
        else if (hour < 18) return TIMELONN + EXTRA2;
        else return TIMELONN + EXTRA1;
    }

    private double calculateSundayPay(int startHour, int endHour) {
        return adjustTotal((TIMELONN + EXTRA1) * (endHour - startHour));
    }

    private double adjustTotal(double total) {
        return Math.round((total - (TIMELONN / 2 + EXTRA1 / 2)) * 100.0) / 100.0;
    }

    private double calculateWeekdayPay(double diffTotalRounded) {
        return Math.round((TIMELONN * diffTotalRounded - TIMELONN / 2) * 100.0) / 100.0;
    }

    private static class Shift {
        int month, year, day, startHour, endHour;
        DayOfWeek dayOfWeek;

        public Shift(int month, int year, int day, int startHour, int endHour) {
            this.month = month;
            this.year = year;
            this.day = day;
            this.startHour = startHour;
            this.endHour = endHour;
            this.dayOfWeek = LocalDate.of(year, month, day).getDayOfWeek();
        }
    }
}

