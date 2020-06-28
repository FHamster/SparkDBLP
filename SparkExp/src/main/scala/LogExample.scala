import org.apache.spark.internal.Logging
import org.apache.log4j.{Level,Logger}


object LogExample extends Logging{
  def main(args: Array[String]){
    def setStreamingLogLevels(){
      val log4jInitiaLized = Logger.getRootLogger.getAllAppenders.hasMoreElements
      if(!log4jInitiaLized){
        logInfo("Setting log level to [Warn] for streaming example" + "To override add a custom log4j.properties to the classpath.")
        Logger.getRootLogger.setLevel(Level.WARN)
      }
    }
  }
}

