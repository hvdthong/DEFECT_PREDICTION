package org.apache.poi.hssf.usermodel;

import org.apache.poi.util.POILogFactory;
import org.apache.poi.hssf.model.Sheet;
import org.apache.poi.hssf.model.Workbook;
import org.apache.poi.hssf.record.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.POILogger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * High level representation of a workbook.  This is the first object most users
 * will construct whether they are reading or writing a workbook.  It is also the
 * top level object for creating new sheets/etc.
 *
 * @see org.apache.poi.hssf.model.Workbook
 * @see org.apache.poi.hssf.usermodel.HSSFSheet
 * @author  Andrew C. Oliver (acoliver at apache dot org)
 * @author  Glen Stampoultzis (glens at apache.org)
 * @version 2.0-pre
 */

public class HSSFWorkbook
        extends java.lang.Object
{
    private static final int DEBUG = POILogger.DEBUG;

    /**
     * used for compile-time performance/memory optimization.  This determines the
     * initial capacity for the sheet collection.  Its currently set to 3.
     * Changing it in this release will decrease performance
     * since you're never allowed to have more or less than three sheets!
     */

    public final static int INITIAL_CAPACITY = 3;

    /**
     * this is the reference to the low level Workbook object
     */

    private Workbook workbook;

    /**
     * this holds the HSSFSheet objects attached to this workbook
     */

    private ArrayList sheets;
    private static POILogger log = POILogFactory.getLogger(HSSFWorkbook.class);

    /**
     * Creates new HSSFWorkbook from scratch (start here!)
     *
     */

    public HSSFWorkbook()
    {
        workbook = Workbook.createWorkbook();
        sheets = new ArrayList(INITIAL_CAPACITY);
    }

    /**
     * given a POI POIFSFileSystem object, read in its Workbook and populate the high and
     * low level models.  If you're reading in a workbook...start here.
     *
     * @param fs the POI filesystem that contains the Workbook stream.
     * @see org.apache.poi.poifs.filesystem.POIFSFileSystem
     * @exception IOException if the stream cannot be read
     */

    public HSSFWorkbook(POIFSFileSystem fs)
            throws IOException
    {
        sheets = new ArrayList(INITIAL_CAPACITY);
        InputStream stream = fs.createDocumentInputStream("Workbook");
        List records = RecordFactory.createRecords(stream);

        workbook = Workbook.createWorkbook(records);
        setPropertiesFromWorkbook(workbook);
        int numRecords = workbook.getNumRecords();
        int sheetNum = 0;

        while (numRecords < records.size())
        {
            Sheet sheet = Sheet.createSheet(records, sheetNum++, numRecords);

            numRecords += sheet.getNumRecords();
            sheet.convertLabelRecords(
            HSSFSheet hsheet = new HSSFSheet(workbook, sheet);

            sheets.add(hsheet);

        }
    }

    /**
     * Companion to HSSFWorkbook(POIFSFileSystem), this constructs the POI filesystem around your
     * inputstream.
     *
     * @param s  the POI filesystem that contains the Workbook stream.
     * @see org.apache.poi.poifs.filesystem.POIFSFileSystem
     * @see #HSSFWorkbook(POIFSFileSystem)
     * @exception IOException if the stream cannot be read
     */

    public HSSFWorkbook(InputStream s)
            throws IOException
    {
        this((new POIFSFileSystem(s)));
    }

    /**
     * used internally to set the workbook properties.
     */

    private void setPropertiesFromWorkbook(Workbook book)
    {
        this.workbook = book;

    }

    /**
     * set the sheet name.
     * @param sheet number (0 based)
     * @param sheet name
     */

    public void setSheetName(int sheet, String name)
    {
        if (sheet > (sheets.size() - 1))
        {
            throw new RuntimeException("Sheet out of bounds");
        }
        workbook.setSheetName(sheet, name);
    }

    /**
     * get the sheet name
     * @param sheet Number
     * @return Sheet name
     */

    public String getSheetName(int sheet)
    {
        if (sheet > (sheets.size() - 1))
        {
            throw new RuntimeException("Sheet out of bounds");
        }
        return workbook.getSheetName(sheet);
    }

    /**
     * get the sheet's index
     * @param name  sheet name
     * @return sheet index or -1 if it was not found.
     */

    public int getSheetIndex(String name)
    {
        int retval = -1;

        for (int k = 0; k < sheets.size(); k++)
        {
            String sheet = workbook.getSheetName(k);

            if (sheet.equals(name))
            {
                retval = k;
                break;
            }
        }
        return retval;
    }

    /**
     * create an HSSFSheet for this HSSFWorkbook, adds it to the sheets and returns
     * the high level representation.  Use this to create new sheets.
     *
     * @return HSSFSheet representing the new sheet.
     */

    public HSSFSheet createSheet()
    {

        HSSFSheet sheet = new HSSFSheet(workbook);

        sheets.add(sheet);
        workbook.setSheetName(sheets.size() - 1,
                "Sheet" + (sheets.size() - 1));
        WindowTwoRecord windowTwo = (WindowTwoRecord) sheet.getSheet().findFirstRecordBySid(WindowTwoRecord.sid);
        windowTwo.setSelected(sheets.size() == 1);
        windowTwo.setPaged(sheets.size() == 1);
        return sheet;
    }

    /**
     * create an HSSFSheet for this HSSFWorkbook, adds it to the sheets and returns
     * the high level representation.  Use this to create new sheets.
     *
     * @param sheetname     sheetname to set for the sheet.
     * @return HSSFSheet representing the new sheet.
     */

    public HSSFSheet createSheet(String sheetname)
    {

        HSSFSheet sheet = new HSSFSheet(workbook);

        sheets.add(sheet);
        workbook.setSheetName(sheets.size() - 1, sheetname);
        WindowTwoRecord windowTwo = (WindowTwoRecord) sheet.getSheet().findFirstRecordBySid(WindowTwoRecord.sid);
        windowTwo.setSelected(sheets.size() == 1);
        windowTwo.setPaged(sheets.size() == 1);
        return sheet;
    }

    /**
     * get the number of spreadsheets in the workbook (this will be three after serialization)
     * @return number of sheets
     */

    public int getNumberOfSheets()
    {
        return sheets.size();
    }

    /**
     * Get the HSSFSheet object at the given index.
     * @param index of the sheet number (0-based physical & logical)
     * @return HSSFSheet at the provided index
     */

    public HSSFSheet getSheetAt(int index)
    {
        return (HSSFSheet) sheets.get(index);
    }

    /**
     * Get sheet with the given name
     * @param name of the sheet
     * @return HSSFSheet with the name provided or null if it does not exist
     */

    public HSSFSheet getSheet(String name)
    {
        HSSFSheet retval = null;

        for (int k = 0; k < sheets.size(); k++)
        {
            String sheetname = workbook.getSheetName(k);

            if (sheetname.equals(name))
            {
                retval = (HSSFSheet) sheets.get(k);
            }
        }
        return retval;
    }

    /**
     * removes sheet at the given index
     * @param index of the sheet  (0-based)
     */

    public void removeSheetAt(int index)
    {
        sheets.remove(index);
        workbook.removeSheet(index);
    }

    /**
     * determine whether the Excel GUI will backup the workbook when saving.
     *
     * @param backupValue   true to indicate a backup will be performed.
     */

    public void setBackupFlag(boolean backupValue)
    {
        BackupRecord backupRecord = workbook.getBackupRecord();

        backupRecord.setBackup(backupValue ? (short) 1
                : (short) 0);
    }

    /**
     * determine whether the Excel GUI will backup the workbook when saving.
     *
     * @return the current setting for backups.
     */

    public boolean getBackupFlag()
    {
        BackupRecord backupRecord = workbook.getBackupRecord();

        return (backupRecord.getBackup() == 0) ? false
                : true;
    }

    /**
     * create a new Font and add it to the workbook's font table
     * @return new font object
     */

    public HSSFFont createFont()
    {
        FontRecord font = workbook.createNewFont();
        short fontindex = (short) (getNumberOfFonts() - 1);

        if (fontindex > 3)
        {
        }
        HSSFFont retval = new HSSFFont(fontindex, font);

        return retval;
    }

    /**
     * get the number of fonts in the font table
     * @return number of fonts
     */

    public short getNumberOfFonts()
    {
        return (short) workbook.getNumberOfFontRecords();
    }

    /**
     * get the font at the given index number
     * @param idx  index number
     * @return HSSFFont at the index
     */

    public HSSFFont getFontAt(short idx)
    {
        FontRecord font = workbook.getFontRecordAt(idx);
        HSSFFont retval = new HSSFFont(idx, font);

        return retval;
    }

    /**
     * create a new Cell style and add it to the workbook's style table
     * @return the new Cell Style object
     */

    public HSSFCellStyle createCellStyle()
    {
        ExtendedFormatRecord xfr = workbook.createCellXF();
        short index = (short) (getNumCellStyles() - 1);
        HSSFCellStyle style = new HSSFCellStyle(index, xfr);

        return style;
    }

    /**
     * get the number of styles the workbook contains
     * @return count of cell styles
     */

    public short getNumCellStyles()
    {
        return (short) workbook.getNumExFormats();
    }

    /**
     * get the cell style object at the given index
     * @param idx  index within the set of styles
     * @return HSSFCellStyle object at the index
     */

    public HSSFCellStyle getCellStyleAt(short idx)
    {
        ExtendedFormatRecord xfr = workbook.getExFormatAt(idx);
        HSSFCellStyle style = new HSSFCellStyle(idx, xfr);

        return style;
    }

    /**
     * Method write - write out this workbook to an Outputstream.  Constructs
     * a new POI POIFSFileSystem, passes in the workbook binary representation  and
     * writes it out.
     *
     * @param stream - the java OutputStream you wish to write the XLS to
     *
     * @exception IOException if anything can't be written.
     * @see org.apache.poi.poifs.filesystem.POIFSFileSystem
     */

    public void write(OutputStream stream)
            throws IOException
    {
        byte[] bytes = getBytes();
        POIFSFileSystem fs = new POIFSFileSystem();

        fs.createDocument(new ByteArrayInputStream(bytes), "Workbook");
        fs.writeFilesystem(stream);
    }

    /**
     * Method getBytes - get the bytes of just the HSSF portions of the XLS file.
     * Use this to construct a POI POIFSFileSystem yourself.
     *
     *
     * @return byte[] array containing the binary representation of this workbook and all contained
     *         sheets, rows, cells, etc.
     *
     * @see org.apache.poi.hssf.model.Workbook
     * @see org.apache.poi.hssf.model.Sheet
     */

    public byte[] getBytes()
    {
        log.log(DEBUG, "HSSFWorkbook.getBytes()");
        int wbsize = workbook.getSize();

        int totalsize = wbsize;

        for (int k = 0; k < sheets.size(); k++)
        {
            workbook.setSheetBof(k, totalsize);

            totalsize += ((HSSFSheet) sheets.get(k)).getSheet().getSize();
        }
        if (totalsize < 4096)
        {
            totalsize = 4096;
        }
        byte[] retval = new byte[totalsize];
        int pos = workbook.serialize(0, retval);

        for (int k = 0; k < sheets.size(); k++)
        {

            pos += ((HSSFSheet) sheets.get(k)).getSheet().serialize(pos,
        }
        for (int k = pos; k < totalsize; k++)
        {
            retval[k] = 0;
        }
        return retval;
    }

    public int addSSTString(String string)
    {
        return workbook.addSSTString(string);
    }

    public String getSSTString(int index)
    {
        return workbook.getSSTString(index);
    }

    Workbook getWorkbook()
    {
        return workbook;
    }
}
