import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.LoggerOps
import akka.actor.typed.{ ActorRef, ActorSystem, Behavior }

case class CrawlerData(referenceCount: Map[Host, Int],
                        visitedLinks: Set[Url],
                        inProgress: Set[Url],
                        workers: Map[Host, ActorRef[WorkerMessage]])


class Crawler(http: Http[Future],
               parseLinks: String => List[Url],
               reportTo: ActorRef[Map[Host, Int]]) {
   def crawlerBehavior: Behavior[CrawlerMessage] = ???
 }

def crawlerBehavior: Behavior[CrawlerMessage] =
   Behaviors.setup[CrawlerMessage] { ctx =>
     def receive(data: CrawlerData): Behavior[CrawlerMessage] = ???
     def crawlUrl(data: CrawlerData, url: Url): CrawlerData = ???
     def workerFor(data: CrawlerData,
                   host: Host): (CrawlerData, ActorRef[WorkerMessage]) = ???
 }


def workerFor(data: CrawlerData, host: Host): (CrawlerData, ActorRef[WorkerMessage]) = {
   data.workers.get(host) match {
     case None =>
       val workerActor = ctx.spawn(workerBehavior(ctx.self), s"worker-$host")
       (data.copy(workers = data.workers + (host -> workerActor)), workerActor)

     case Some(ar) => (data, ar)
   }
}

def receive(data: CrawlerData): Behavior[CrawlerMessage] = Behaviors.receiveMessage {
  case Start(start) =>
    receive(crawlUrl(data, start))

  case CrawlResult(url, links) =>
    val data2 = data.copy(inProgress = data.inProgress - url)

    val data3 = links.foldLeft(data2) {
      case (d, link) =>
         val d2 = d.copy(referenceCount = d.referenceCount.updated(
           link.host, d.referenceCount.getOrElse(link.host, 0) + 1))
         crawlUrl(d2, link)
     }

     if (data3.inProgress.isEmpty) {
       reportTo ! data3.referenceCount
       Behavior.stopped
     } else {
       receive(data3)
     }
 }

 def crawlUrl(data: CrawlerData, url: Url): CrawlerData = {
   if (!data.visitedLinks.contains(url)) {
     val (data2, worker) = workerFor(data, url.host)
     worker ! Crawl(url)
     data2.copy(
       visitedLinks = data.visitedLinks + url,
       inProgress = data.inProgress + url
     )
   } else data
 }

def workerBehavior(master: ActorRef[CrawlResult]): Behavior[WorkerMessage] =
  Behaviors.setup[WorkerMessage] { ctx =>

def receive(urlsPending: Vector[Url], getInProgress: Boolean): Behavior[WorkerMessage] =
    Behaviors.receiveMessage {
      case Crawl(url) =>
        startHttpGetIfPossible(urlsPending :+ url, getInProgress)

      case HttpGetResult(url, Success(body)) =>
         val links = parseLinks(body)
         master ! CrawlResult(url, links)

         startHttpGetIfPossible(urlsPending, getInProgress = false)

       case HttpGetResult(url, Failure(e)) =>
         ctx.log.error(s"Cannot get contents of $url", e)
         master ! CrawlResult(url, Nil)

         startHttpGetIfPossible(urlsPending, getInProgress = false)
     }

def startHttpGetIfPossible(urlsPending: Vector[Url],
                              getInProgress: Boolean): Behavior[WorkerMessage] =
     urlsPending match {
       case url +: tail if !getInProgress =>
         import ctx.executionContext
         http.get(url).onComplete(r => ctx.self ! HttpGetResult(url, r))

         receive(tail, getInProgress = true)

       case _ =>
         receive(urlsPending, getInProgress)
     }

   receive(Vector.empty, getInProgress = false)
 }

object web_crawler_object {
  def main() {
    cwlr = new Crawler(http, parseLinks, reportTo)
  }
}
