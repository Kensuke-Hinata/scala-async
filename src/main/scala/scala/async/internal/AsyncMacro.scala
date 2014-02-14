package scala.async.internal

import scala.tools.nsc.Global
import scala.tools.nsc.transform.TypingTransformers

object AsyncMacro {
  def apply(c0: reflect.macros.Context, base: AsyncBase): AsyncMacro = {
    import language.reflectiveCalls
    val powerContext = c0.asInstanceOf[c0.type { val universe: Global; val callsiteTyper: universe.analyzer.Typer }]
    new AsyncMacro { self =>
      val c: scala.reflect.macros.Context { val universe: global.type } = c0.asInstanceOf[scala.reflect.macros.Context { val universe: global.type }]
      val global: powerContext.universe.type   = powerContext.universe
      // This member is required by `AsyncTransform`:
      val asyncBase: AsyncBase                 = base
      // These members are required by `ExprBuilder`:
      val futureSystem: FutureSystem           = base.futureSystem
      val futureSystemOps: futureSystem.Ops {val c: self.c.type} = futureSystem.mkOps(c)
    }
  }
}

private[async] trait AsyncMacro
  extends AnfTransform with TransformUtils with Lifter
  with ExprBuilder with AsyncTransform with AsyncAnalysis with LiveVariables {

  val c: scala.reflect.macros.Context { val universe: global.type }
  val global: Global

  lazy val macroPos = c.macroApplication.pos.makeTransparent
  def atMacroPos(t: global.Tree) = c.universe.atPos(macroPos)(t)
}
