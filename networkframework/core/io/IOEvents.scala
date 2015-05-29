package networkframework.core.io

import networkframework.core.monitoring.Event
case class GraphImportedEvent() extends Event{
	key = "io.graphImported"
}

case class GraphExportedEvent() extends Event{
	key = "io.graphExported"
}