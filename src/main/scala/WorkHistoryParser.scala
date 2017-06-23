import java.io.File
import java.time.LocalDate

import scala.xml.{Elem, XML}

object WorkHistoryParser {

  implicit val localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)

  def main(args: Array[String]): Unit = {
    val historyFilePath = getClass.getResource("/work-history.xml").getPath
    val history: Elem = XML.loadFile(new File(historyFilePath))

    val feed = history \\ "feed"

    val workingDays = (feed \\ "entry")
      .groupBy(e => (e \\ "published").text.split("T")(0))
      .map(day => {
        val tickets: Seq[WorkedTicket] = day._2.map(ticket => {
          (ticket \\ "object").filter(_.prefix == "activity")
        }).map(activity => {
          val title = (activity \\ "title").text
          val summary = (activity \\ "summary").text
          (title, summary)
        }).filter(t => !t._1.isEmpty && !t._2.isEmpty)
          .map(t => WorkedTicket(t._1, t._2))

        WorkingDay(LocalDate.parse(day._1), tickets.toSet)
      }).toList

    workingDays
      .sortBy(_.day)
      .foreach(wd => println(wd.toString))
  }
}