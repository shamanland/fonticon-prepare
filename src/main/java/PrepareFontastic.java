import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PrepareFontastic {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Specify path to archive downloaded from http://fontastic.me/app/");
            return;
        }

        File file = new File(args[0]);
        if (!file.canRead()) {
            System.out.println("Can not read file: " + file.getAbsolutePath());
            return;
        }

        ZipFile zip = new ZipFile(args[0]);
        Enumeration<? extends ZipEntry> it = zip.entries();

        while (it.hasMoreElements()) {
            ZipEntry e = it.nextElement();

            if (e.getName().endsWith(".ttf")) {
                processFont(zip.getInputStream(e));
            } else if (e.getName().equals("styles.css")) {
                processStyles(zip.getInputStream(e));
            }
        }
    }

    private static void processFont(InputStream is) throws IOException {
        OutputStream os = null;

        try {
            os = new FileOutputStream("icons.ttf");

            byte[] buf = new byte[1024];
            int read;

            while ((read = is.read(buf)) > 0) {
                os.write(buf, 0, read);
            }
        } finally {
            close(is);
            close(os);
        }
    }

    private static void processStyles(InputStream is) throws IOException {
        PrintStream os = null;

        try {
            os = new PrintStream("icons.xml");
            os.println("<resources>");

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String name = null;

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.length() == 0) {
                    // skip empty line
                } else if (line.charAt(0) == '.') {
                    int nameEnd = line.indexOf(":before {");
                    if (nameEnd > 1) {
                        name = line.substring(1, nameEnd);
                        name = name.replaceAll("-", "_");
                    }
                } else if (line.startsWith("content:")) {
                    if (name != null) {
                        int valueBegin = line.indexOf("\"");
                        int valueEnd = line.lastIndexOf("\"");

                        if (valueBegin + 1 < valueEnd) {
                            String value = line.substring(valueBegin + 1, valueEnd);
                            if (value.startsWith("\\") && value.length() == 5) {
                                value = "&#x" + value.substring(1);
                                value += ";";
                            }

                            os.print("    <string name=\"");
                            os.print(name);
                            os.print("\">");
                            os.print(value);
                            os.println("</string>");
                        }
                    }
                }
            }
        } finally {
            if (os != null) {
                os.println("</resources>");
            }

            close(is);
            close(os);
        }
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
