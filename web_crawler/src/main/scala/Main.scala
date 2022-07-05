import org.jsoup.Jsoup
import java.net._
import scala.collection.JavaConverters._

case class web_crawler() {
  def get_sub_links(urlToBeProcessed: String):Option[List[String]] = {
    try {
      val connection = Jsoup.connect(urlToBeProcessed)
      val document = connection.get()
      val hyperlinks = document.select("a[href]")
      val scala_hyperlinks = hyperlinks.asScala
      val hyperlinks_as_map = scala_hyperlinks.map(_.attr("abs:href")).toSeq
      val hyperlink_objects = hyperlinks_as_map.map(new URL(_))
      val hostURL = (new URL(urlToBeProcessed)).getHost
      val matching_hyperlinks = hyperlink_objects.filter(_.getHost == hostURL).map(_.toString).toList
      Some(matching_hyperlinks)
    }
    catch {
      case e: Exception => None
    }
  }

  def traverse(link_list: Option[List[String]], accumulator: List[String]): List[String] = link_list match {
    case Some(Nil) => accumulator
    case Some(head::tail) => if (!accumulator.contains(head))
    traverse(get_sub_links(head), head::accumulator)
  else
    traverse(Option(tail), accumulator)
    case None => accumulator
  }
}

object web_crawler_object extends web_crawler {
  def main(args: Array[String]) = {
    val mURL = args(0)
    traverse(get_sub_links(mURL), List(mURL))
  }
}
