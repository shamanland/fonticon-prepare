import java.util.Locale;

public abstract class Preparation {
    public abstract void prepare(String archiveName) throws Throwable;

    public static void main(String[] args) throws Throwable {
        if (args.length < 2) {
            System.out.println("Usage:");
            System.out.println("    <preparation> - currently supports only 'fontastic'");
            System.out.println("    <archive>     - name of downloaded archive");
            return;
        }

        if (args[0].length() < 2) {
            System.err.println("too short preparation name");
            return;
        }

        String preparationName = args[0].substring(0, 1).toUpperCase(Locale.US) + args[0].substring(1);
        Preparation preparation = (Preparation) Class.forName("Preparation" + preparationName).newInstance();
        preparation.prepare(args[1]);
    }
}
