import java.time.LocalDate
import java.time.format.DateTimeFormatter

case class WorkingDay(var day: LocalDate, var tickets: Set[WorkedTicket]) {
  override def toString: String = {
    return day.format(DateTimeFormatter.ISO_DATE) + ":\n" + tickets
      .map(t => t.ticket + " " + t.summary).mkString("\n") + "\n\n"
  }
}

case class WorkedTicket(var ticket: String, var summary: String) {
}
