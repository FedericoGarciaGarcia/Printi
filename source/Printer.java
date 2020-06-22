import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import javax.print.*;
import java.awt.Color;
import java.awt.geom.AffineTransform;

public class Printer implements Runnable {

    private BufferedImage image;
    private PrintService printService;
	private Config config;
	private TrayIcon trayIcon;

    public Printer(BufferedImage image, PrintService printService, Config config, TrayIcon trayIcon) {
        this.image = image;
        this.printService = printService;
		this.config = config;
		this.trayIcon = trayIcon;
    }

    @Override
    public void run() {
        // Print without dialog
        PrinterJob printJob = PrinterJob.getPrinterJob();
        
        try {
			int realOrientation;
			
			if(config.orientation == 0)
				realOrientation = PageFormat.LANDSCAPE;
			else
				realOrientation = PageFormat.PORTRAIT;
			
			PageSize pageSize = PageSize.A4;
			if(config.paperSize.equals("B5")) pageSize = PageSize.B5;
			if(config.paperSize.equals("A4")) pageSize = PageSize.A4;
			if(config.paperSize.equals("B4")) pageSize = PageSize.B4;
			if(config.paperSize.equals("A3")) pageSize = PageSize.A3;
			
			PageFormatFactory pageFormatFactory = PageFormatFactory.getInstance();
			Paper paper = pageFormatFactory.createPaper (pageSize);
			pageFormatFactory.setBordersMm (paper, 0, 0, 0, 0);
			PageFormat format = pageFormatFactory.createPageFormat (paper, realOrientation);
			
            //set the printService found (should be tested)
			printJob.setPrintable(new ImagePrintable(printJob, format, image, config.fit), format);
            printJob.setPrintService(printService);
        
			if(config.printDialog == 0) {
				if (printJob.printDialog()) {
					try {
						printJob.print();
						
						if(config.notification == 0)
						trayIcon.displayMessage("Printi", "'"+printService.getName()+"' is printing", TrayIcon.MessageType.INFO);
					} catch (PrinterException prt) {
						prt.printStackTrace();
					}
				}
			}
			else {
				if(config.notification == 0)
				trayIcon.displayMessage("Printi", "'"+printService.getName()+"' is printing", TrayIcon.MessageType.INFO);

				printJob.print();
				
			}
        } catch (PrinterException prt) {
            prt.printStackTrace();
        }
    }

    public class ImagePrintable implements Printable {

        private double x, y, width, height;
        private int orientation;
        private BufferedImage image;
		private int fit;

        public ImagePrintable(PrinterJob printJob, PageFormat pageFormat, BufferedImage image, int fit) {
            this.x = pageFormat.getImageableX();
            this.y = pageFormat.getImageableY();
            this.width = pageFormat.getImageableWidth();
            this.height = pageFormat.getImageableHeight();
            this.orientation = pageFormat.getOrientation();
			this.image = image;
			this.fit = fit;
        }

        @Override
        public int print(Graphics g, PageFormat pageFormat, int pageIndex)
                throws PrinterException {
            if (pageIndex == 0) {
                int pWidth = 0;
                int pHeight = 0;
				
				if(fit == 0) {
					// Determine best fit
					pWidth = (int) Math.min(width, (double) image.getWidth());
					pHeight = pWidth * image.getHeight() / image.getWidth();
					
					if(pHeight > height) {
						pHeight = (int) Math.min(width, (double) image.getHeight());
						pWidth = pHeight * image.getWidth() / image.getHeight();
					}
				}
				else {
					pWidth  = (int) width;
					pHeight = (int) height;
				}
				
                g.drawImage(image, (int) x, (int) y, pWidth, pHeight, null);
				
                return PAGE_EXISTS;
            } else {
                return NO_SUCH_PAGE;
            }
        }

    }
}