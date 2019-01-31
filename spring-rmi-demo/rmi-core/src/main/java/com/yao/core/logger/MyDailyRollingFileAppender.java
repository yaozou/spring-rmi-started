/**
 * 
 */
package com.yao.core.logger;

import com.yao.core.RmiThreadLocal;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/** 
* MyDailyRollingFileAppender extends {@link FileAppender} so that the underlying 
* file is rolled over at a user chosen frequency. 
*  
* <p> 
* The rolling schedule is specified by the <b>DatePattern</b> option. This 
* pattern should follow the {@link SimpleDateFormat} conventions. In 
* particular, you <em>must</em> escape literal text within a pair of single 
* quotes. A formatted version of the date pattern is used as the suffix for the 
* rolled file name. 
*  
* <p> 
* For example, if the <b>File</b> option is set to <code>/foo/bar.log</code> 
* and the <b>DatePattern</b> set to <code>'.'yyyy-MM-dd</code>, on 2001-02-16 
* at midnight, the logging file <code>/foo/bar.log</code> will be copied to 
* <code>/foo/bar.log.2001-02-16</code> and logging for 2001-02-17 will continue 
* in <code>/foo/bar.log</code> until it rolls over the next day. 
*  
* <p> 
* Is is possible to specify monthly, weekly, half-daily, daily, hourly, or 
* minutely rollover schedules. 
*  
* <p> 
* <table border="1" cellpadding="2"> 
* <tr> 
* <th>DatePattern</th> 
* <th>Rollover schedule</th> 
* <th>Example</th> 
*  
* <tr> 
* <td><code>'.'yyyy-MM</code> 
* <td>Rollover at the beginning of each month</td> 
*  
* <td>At midnight of May 31st, 2002 <code>/foo/bar.log</code> will be copied to 
* <code>/foo/bar.log.2002-05</code>. Logging for the month of June will be 
* output to <code>/foo/bar.log</code> until it is also rolled over the next 
* month. 
*  
* <tr> 
* <td><code>'.'yyyy-ww</code> 
*  
* <td>Rollover at the first day of each week. The first day of the week depends 
* on the locale.</td> 
*  
* <td>Assuming the first day of the week is Sunday, on Saturday midnight, June 
* 9th 2002, the file <i>/foo/bar.log</i> will be copied to 
* <i>/foo/bar.log.2002-23</i>. Logging for the 24th week of 2002 will be output 
* to <code>/foo/bar.log</code> until it is rolled over the next week. 
*  
* <tr> 
* <td><code>'.'yyyy-MM-dd</code> 
*  
* <td>Rollover at midnight each day.</td> 
*  
* <td>At midnight, on March 8th, 2002, <code>/foo/bar.log</code> will be copied 
* to <code>/foo/bar.log.2002-03-08</code>. Logging for the 9th day of March 
* will be output to <code>/foo/bar.log</code> until it is rolled over the next 
* day. 
*  
* <tr> 
* <td><code>'.'yyyy-MM-dd-a</code> 
*  
* <td>Rollover at midnight and midday of each day.</td> 
*  
* <td>At noon, on March 9th, 2002, <code>/foo/bar.log</code> will be copied to 
* <code>/foo/bar.log.2002-03-09-AM</code>. Logging for the afternoon of the 9th 
* will be output to <code>/foo/bar.log</code> until it is rolled over at 
* midnight. 
*  
* <tr> 
* <td><code>'.'yyyy-MM-dd-HH</code> 
*  
* <td>Rollover at the top of every hour.</td> 
*  
* <td>At approximately 11:00.000 o'clock on March 9th, 2002, 
* <code>/foo/bar.log</code> will be copied to 
* <code>/foo/bar.log.2002-03-09-10</code>. Logging for the 11th hour of the 9th 
* of March will be output to <code>/foo/bar.log</code> until it is rolled over 
* at the beginning of the next hour. 
*  
*  
* <tr> 
* <td><code>'.'yyyy-MM-dd-HH-mm</code> 
*  
* <td>Rollover at the beginning of every minute.</td> 
*  
* <td>At approximately 11:23,000, on March 9th, 2001, <code>/foo/bar.log</code> 
* will be copied to <code>/foo/bar.log.2001-03-09-10-22</code>. Logging for the 
* minute of 11:23 (9th of March) will be output to <code>/foo/bar.log</code> 
* until it is rolled over the next minute. 
*  
* </table> 
*  
* <p> 
* Do not use the colon ":" character in anywhere in the <b>DatePattern</b> 
* option. The text before the colon is interpeted as the protocol specificaion 
* of a URL which is probably not what you want. 
*/  
public class MyDailyRollingFileAppender extends FileAppender {  

  // The code assumes that the following constants are in a increasing  
  // sequence.  
  static final int TOP_OF_TROUBLE = -1;  
  static final int TOP_OF_MINUTE = 0;  
  static final int TOP_OF_HOUR = 1;  
  static final int HALF_DAY = 2;  
  static final int TOP_OF_DAY = 3;  
  static final int TOP_OF_WEEK = 4;  
  static final int TOP_OF_MONTH = 5;  

  /** 
   * The default maximum file size is 10MB. 
   */  
  protected long maxFileSize = 10 * 1024 * 1024;  

  /** 
   * There is one backup file by default. 
   */  
  protected int maxBackupIndex = 1;  

  /** 
   * The date pattern. By default, the pattern is set to "'.'yyyy-MM-dd" 
   * meaning daily rollover. 
   */  
  private String datePattern = "'.'yyyy-MM-dd";  

  /** 
   * The log file will be renamed to the value of the scheduledFilename 
   * variable when the next interval is entered. For example, if the rollover 
   * period is one hour, the log file will be renamed to the value of 
   * "scheduledFilename" at the beginning of the next hour. 
   *  
   * The precise time when a rollover occurs depends on logging activity. 
   */  
  private String scheduledFilename;  

  /** 
   * The next time we estimate a rollover should occur. 
   */  
  private long nextCheck = System.currentTimeMillis() - 1;  

  Date now = new Date();  

  SimpleDateFormat sdf;  

  RollingCalendar rc = new RollingCalendar();  

  int checkPeriod = TOP_OF_TROUBLE;  

  // The gmtTimeZone is used only in computeCheckPeriod() method.  
  static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");  

  /** 
   * The default constructor does nothing. 
   */  
  public MyDailyRollingFileAppender() {  
  }  

  /** 
   * Instantiate a <code>MyDailyRollingFileAppender</code> and open the file 
   * designated by <code>filename</code>. The opened filename will become the 
   * ouput destination for this appender. 
   */  
  public MyDailyRollingFileAppender(Layout layout, String filename,  
          String datePattern) throws IOException {  
      super(layout, filename, true);  
      this.datePattern = datePattern;  
      activateOptions();  
  }  

  /** 
   * Get the maximum size that the output file is allowed to reach before 
   * being rolled over to backup files. 
   *  
   * @since 1.1 
   */  
  public long getMaximumFileSize() {  
      return maxFileSize;  
  }  

  /** 
   * Set the maximum size that the output file is allowed to reach before 
   * being rolled over to backup files. 
   *  
   * <p> 
   * This method is equivalent to {@link #setMaxFileSize} except that it is 
   * required for differentiating the setter taking a <code>long</code> 
   * argument from the setter taking a <code>String</code> argument by the 
   * JavaBeans {@link java.beans.Introspector Introspector}. 
   *  
   * @see #setMaxFileSize(String) 
   */  
  public void setMaximumFileSize(long maxFileSize) {  
      this.maxFileSize = maxFileSize;  
  }  

  /** 
   * Set the maximum size that the output file is allowed to reach before 
   * being rolled over to backup files. 
   *  
   * <p> 
   * In configuration files, the <b>MaxFileSize</b> option takes an long 
   * integer in the range 0 - 2^63. You can specify the value with the 
   * suffixes "KB", "MB" or "GB" so that the integer is interpreted being 
   * expressed respectively in kilobytes, megabytes or gigabytes. For example, 
   * the value "10KB" will be interpreted as 10240. 
   */  
  public void setMaxFileSize(String value) {  
      maxFileSize = OptionConverter.toFileSize(value, maxFileSize + 1);  
  }  

  /** 
   * Returns the value of the <b>MaxBackupIndex</b> option. 
   */  
  public int getMaxBackupIndex() {  
      return maxBackupIndex;  
  }  

  /** 
   * Set the maximum number of backup files to keep around. 
   *  
   * <p> 
   * The <b>MaxBackupIndex</b> option determines how many backup files are 
   * kept before the oldest is erased. This option takes a positive integer 
   * value. If set to zero, then there will be no backup files and the log 
   * file will be truncated when it reaches <code>MaxFileSize</code>. 
   */  
  public void setMaxBackupIndex(int maxBackups) {  
      this.maxBackupIndex = maxBackups;  
  }  

  /** 
   * The <b>DatePattern</b> takes a string in the same format as expected by 
   * {@link SimpleDateFormat}. This options determines the rollover schedule. 
   */  
  public void setDatePattern(String pattern) {  
      datePattern = pattern;  
  }  

  /** Returns the value of the <b>DatePattern</b> option. */  
  public String getDatePattern() {  
      return datePattern;  
  }  

  @Override
  public void activateOptions() {  
      super.activateOptions();  
      if (datePattern != null && fileName != null) {  
          now.setTime(System.currentTimeMillis());  
          sdf = new SimpleDateFormat(datePattern);  
          int type = computeCheckPeriod();  
          printPeriodicity(type);  
          rc.setType(type);  
          File file = new File(fileName);  
          scheduledFilename = fileName  
                  + sdf.format(new Date(file.lastModified()));  

      } else {  
          LogLog.error("Either File or DatePattern options are not set for appender ["  
                  + name + "].");  
      }  
  }  

  void printPeriodicity(int type) {  
      switch (type) {  
      case TOP_OF_MINUTE:  
          LogLog.debug("Appender [" + name + "] to be rolled every minute.");  
          break;  
      case TOP_OF_HOUR:  
          LogLog.debug("Appender [" + name  
                  + "] to be rolled on top of every hour.");  
          break;  
      case HALF_DAY:  
          LogLog.debug("Appender [" + name  
                  + "] to be rolled at midday and midnight.");  
          break;  
      case TOP_OF_DAY:  
          LogLog.debug("Appender [" + name + "] to be rolled at midnight.");  
          break;  
      case TOP_OF_WEEK:  
          LogLog.debug("Appender [" + name  
                  + "] to be rolled at start of week.");  
          break;  
      case TOP_OF_MONTH:  
          LogLog.debug("Appender [" + name  
                  + "] to be rolled at start of every month.");  
          break;  
      default:  
          LogLog.warn("Unknown periodicity for appender [" + name + "].");  
      }  
  }  

  // This method computes the roll over period by looping over the  
  // periods, starting with the shortest, and stopping when the r0 is  
  // different from from r1, where r0 is the epoch formatted according  
  // the datePattern (supplied by the user) and r1 is the  
  // epoch+nextMillis(i) formatted according to datePattern. All date  
  // formatting is done in GMT and not local format because the test  
  // logic is based on comparisons relative to 1970-01-01 00:00:00  
  // GMT (the epoch).  

  int computeCheckPeriod() {  
      RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone,  
              Locale.ENGLISH);  
      // set sate to 1970-01-01 00:00:00 GMT  
      Date epoch = new Date(0);  
      if (datePattern != null) {  
          for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {  
              SimpleDateFormat simpleDateFormat = new SimpleDateFormat(  
                      datePattern);  
              simpleDateFormat.setTimeZone(gmtTimeZone); // do all date  
                                                          // formatting in GMT  
              String r0 = simpleDateFormat.format(epoch);  
              rollingCalendar.setType(i);  
              Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));  
              String r1 = simpleDateFormat.format(next);  
              // System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);  
              if (r0 != null && r1 != null && !r0.equals(r1)) {  
                  return i;  
              }  
          }  
      }  
      return TOP_OF_TROUBLE; // Deliberately head for trouble...  
  }  

  /** 
   * Implements the usual roll over behaviour. 
   *  
   * <p> 
   * If <code>MaxBackupIndex</code> is positive, then files { 
   * <code>File.1</code>, ..., <code>File.MaxBackupIndex -1</code> are renamed 
   * to {<code>File.2</code>, ..., <code>File.MaxBackupIndex</code> . 
   * Moreover, <code>File</code> is renamed <code>File.1</code> and closed. A 
   * new <code>File</code> is created to receive further log output. 
   *  
   * <p> 
   * If <code>MaxBackupIndex</code> is equal to zero, then the 
   * <code>File</code> is truncated with no backup files created. 
   */  
  public// synchronization not necessary since doAppend is alreasy synched  
  void sizeRollOver() {  
      File target;  
      File file;  

      LogLog.debug("rolling over count="  
              + ((CountingQuietWriter) qw).getCount());  
      LogLog.debug("maxBackupIndex=" + maxBackupIndex);  

      String datedFilename = fileName + sdf.format(now);  

      if (maxBackupIndex > 0) {  
          // Delete the oldest file, to keep Windows happy.  
          file = new File(datedFilename + '.' + maxBackupIndex);  
          if (file.exists())  
              file.delete();

          // Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3,  
          // 2}  
          for (int i = maxBackupIndex - 1; i >= 1; i--) {  
              file = new File(datedFilename + "." + i);  
              if (file.exists()) {  
                  target = new File(datedFilename + '.' + (i + 1));  
                  LogLog.debug("Renaming file " + file + " to " + target);  
                  file.renameTo(target);  
              }  
          }  

          // Rename fileName to datedFilename.1  
          target = new File(datedFilename + "." + 1);  

          this.closeFile(); // keep windows happy.  

          file = new File(fileName);  
          LogLog.debug("Renaming file " + file + " to " + target);  
          file.renameTo(target);  
      }else if (maxBackupIndex < 0){//infinite number of files   
          //find the max backup index  
          for (int i = 1; i < Integer.MAX_VALUE; i++) {  
              target = new File(datedFilename + "." + i);  
              if (! target.exists()) {//Rename fileName to datedFilename.i  
                  this.closeFile();  
                  file = new File(fileName);  
                  file.renameTo(target);  
                  LogLog.debug("Renaming file " + file + " to " + target);  
                  break;  
              }  
          }  
      }  

      try {  
          // This will also close the file. This is OK since multiple  
          // close operations are safe.  
          this.setFile(fileName, false, bufferedIO, bufferSize);  
      } catch (IOException e) {  
          LogLog.error("setFile(" + fileName + ", false) call failed.", e);  
      }  
      scheduledFilename = datedFilename;  
  }  

  @Override
  public synchronized void setFile(String fileName, boolean append,  
          boolean bufferedIO, int bufferSize) throws IOException {  
      super.setFile(fileName, append, this.bufferedIO, this.bufferSize);  
      if (append) {  
          File f = new File(fileName);  
          ((CountingQuietWriter) qw).setCount(f.length());  
      }  
  }  

  @Override
  protected void setQWForFiles(Writer writer) {  
      this.qw = new CountingQuietWriter(writer, errorHandler);  
  }  

  /** 
   * Rollover the current file to a new file. 
   */  
  void timeRollOver() throws IOException {  

      /* Compute filename, but only if datePattern is specified */  
      if (datePattern == null) {  
          errorHandler.error("Missing DatePattern option in rollOver().");  
          return;  
      }  

      String datedFilename = fileName + sdf.format(now);  
      // It is too early to roll over because we are still within the  
      // bounds of the current interval. Rollover will occur once the  
      // next interval is reached.  
      if (scheduledFilename.equals(datedFilename)) {  
          return;  
      }  

      // close current file, and rename it to datedFilename  
      this.closeFile();  

      File target = new File(scheduledFilename);  
      if (target.exists()) {  
          target.delete();  
      }  

      File file = new File(fileName);  
      boolean result = file.renameTo(target);  
      if (result) {  
          LogLog.debug(fileName + " -> " + scheduledFilename);  
      } else {  
          LogLog.error("Failed to rename [" + fileName + "] to ["  
                  + scheduledFilename + "].");  
      }  

      try {  
          // This will also close the file. This is OK since multiple  
          // close operations are safe.  
          super.setFile(fileName, false, this.bufferedIO, this.bufferSize);  
      } catch (IOException e) {  
          errorHandler.error("setFile(" + fileName + ", false) call failed.");  
      }  
      scheduledFilename = datedFilename;  
  }  

  /** 
   * This method differentiates MyDailyRollingFileAppender from its super class. 
   *  
   * <p> 
   * Before actually logging, this method will check whether it is time to do 
   * a rollover. If it is, it will schedule the next rollover time and then 
   * rollover. 
   * */
  @Override
  protected void subAppend(LoggingEvent event) {  
      long n = System.currentTimeMillis();  

      if (n >= nextCheck) {  
          now.setTime(n);  
          nextCheck = rc.getNextCheckMillis(now);  
          try {  
              timeRollOver();  
          } catch (IOException ioe) {  
              LogLog.error("rollOver() failed.", ioe);  
          }  
      } else if ((fileName != null)  
              && ((CountingQuietWriter) qw).getCount() >= maxFileSize) {  
          sizeRollOver();  
      }

//      event.setProperty("request-id", RmiThreadLocal.get());
      super.subAppend(event);
  }  
}

/** 
* RollingCalendar is a helper class to MyDailyRollingFileAppender. Given a 
* periodicity type and the current time, it computes the start of the next 
* interval. 
* */  
class RollingCalendar extends GregorianCalendar {  

  /**
	 * 
	 */
	private static final long serialVersionUID = -7325917306987507321L;
int type = MyDailyRollingFileAppender.TOP_OF_TROUBLE;  

  RollingCalendar() {  
      super();  
  }  

  RollingCalendar(TimeZone tz, Locale locale) {  
      super(tz, locale);  
  }  

  void setType(int type) {  
      this.type = type;  
  }  

  public long getNextCheckMillis(Date now) {  
      return getNextCheckDate(now).getTime();  
  }  

  public Date getNextCheckDate(Date now) {  
      this.setTime(now);  

      switch (type) {  
      case MyDailyRollingFileAppender.TOP_OF_MINUTE:  
          this.set(Calendar.SECOND, 0);  
          this.set(Calendar.MILLISECOND, 0);  
          this.add(Calendar.MINUTE, 1);  
          break;  
      case MyDailyRollingFileAppender.TOP_OF_HOUR:  
          this.set(Calendar.MINUTE, 0);  
          this.set(Calendar.SECOND, 0);  
          this.set(Calendar.MILLISECOND, 0);  
          this.add(Calendar.HOUR_OF_DAY, 1);  
          break;  
      case MyDailyRollingFileAppender.HALF_DAY:  
          this.set(Calendar.MINUTE, 0);  
          this.set(Calendar.SECOND, 0);  
          this.set(Calendar.MILLISECOND, 0);  
          int hour = get(Calendar.HOUR_OF_DAY);  
          if (hour < 12) {  
              this.set(Calendar.HOUR_OF_DAY, 12);  
          } else {  
              this.set(Calendar.HOUR_OF_DAY, 0);  
              this.add(Calendar.DAY_OF_MONTH, 1);  
          }  
          break;  
      case MyDailyRollingFileAppender.TOP_OF_DAY:  
          this.set(Calendar.HOUR_OF_DAY, 0);  
          this.set(Calendar.MINUTE, 0);  
          this.set(Calendar.SECOND, 0);  
          this.set(Calendar.MILLISECOND, 0);  
          this.add(Calendar.DATE, 1);  
          break;  
      case MyDailyRollingFileAppender.TOP_OF_WEEK:  
          this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());  
          this.set(Calendar.HOUR_OF_DAY, 0);  
          this.set(Calendar.SECOND, 0);  
          this.set(Calendar.MILLISECOND, 0);  
          this.add(Calendar.WEEK_OF_YEAR, 1);  
          break;  
      case MyDailyRollingFileAppender.TOP_OF_MONTH:  
          this.set(Calendar.DATE, 1);  
          this.set(Calendar.HOUR_OF_DAY, 0);  
          this.set(Calendar.SECOND, 0);  
          this.set(Calendar.MILLISECOND, 0);  
          this.add(Calendar.MONTH, 1);  
          break;  
      default:  
          throw new IllegalStateException("Unknown periodicity type.");  
      }  
      return getTime();  
  }  
}