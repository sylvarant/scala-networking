package networkframework.core.graph

trait Visitor {
	def visit(g : Graph);
	def visit(n : Node);
	def visit(e : Edge);
}