case class web_crawler() {
  def get_sub_links(urlToBeProcessed: String):Option[List[String]] = {
    try {
      val connection = Jsoup.connect(urlToBeProcessed)
      val document = connection.get()
      val hyperlinks = doc.select("a[href]")
    }
    catch {
      case e: Exception => None
    }
    val scala_hyperlinks = elements.asScala
    val hyperlinks_as_map = scala_hyperlinks.map(_.attr("abs:href")).toSeq
    val hyperlink_objects = hyperlinks_as_map.map(new URL(_))
    val hostURL = (new URL(urlToBeProcessed)).getHost
    val matching_hyperlinks = hyperlinks_as_map.filter(_.getHost == hostURL).map(_.toString).toList
    Some(matching_hyperlinks)
  }

  def traverse(link_list: Option[List[String]], accumulator: List[String]): List[String] = link_list match {
    case Some(Nil) => acc
    case Some(head::tail) => if (!accumulator.contains(head))
    traverse(get_sub_links(head), head::accumulator)
  else
    traverse(Option(tail), accumulator)
    case None => accumulator
  }
}

object {
  mURL = args(0)
  traverse(get_sub_links(mURL), List(mURL))
}
