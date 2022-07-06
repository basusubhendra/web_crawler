class WebCrawlerSuite extends WordSpec with MockFactory  {
  trait HyperLinksFixture {
    val connection = mock[Connection]
    val getConnection = mockFunction[String, Connection]
    lazy val web_crawler = new WebCrawler() {
      override def getConnection(url_to_be_processed: String) = 
        HyperLinksFixture.this.getConnection(url_to_be_processed) 
    }
  }
  trait TraverseFixture {
    val getHyperLinksPage = mock[String, Option[List[String]]]
    lazy val web_crawler = new Web_Crawler() {
      override def getHyperLinksPage(url_to_be_processed: String) =
        TraverseFixture.this.getHyperLinksPage(url_to_be_processed)
    }
  }

  "get_sub_links" should {
      "return the links" in new HyperLinksFixture {
        val url_to_be_processed = "https://www.wikipedia.org/"
        getConnection expects(url_to_be_processed) returning connection
        web_crawler.get_sub_links(url_to_be_processed) shouldBe expected
      }
  }

  "traverse" should {
    "traverse over the hyperlinks" in new TraverseFixture {
      get_sub_links expects(*) onCall {
        _ match {
          case "a" => Some(List("b", "c"))
          case "b" => Some(List("d"))
          case _ => None
        }
      }
      web_crawler.traverse(Some(List("a")), List.empty[String]) shouldBe
    }
  }
}
