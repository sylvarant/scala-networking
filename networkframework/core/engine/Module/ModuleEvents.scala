package networkframework.core.engine.Module

import networkframework.core.monitoring.Event

case class ModuleInstalledEvent(m: Module) extends Event {
	key = "module.Installed"
}

case class ModuleXInstalledEvent(m: Module) extends Event {
	key = "module."+ m.key +"Installed"
}