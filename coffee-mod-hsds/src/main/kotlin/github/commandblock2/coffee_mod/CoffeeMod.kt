package github.commandblock2.coffee_mod
import net.fabricmc.api.ModInitializer
@Suppress("UNUSED")
object CoffeeMod: ModInitializer {
    private const val MOD_ID = "coffee_mod"
    override fun onInitialize() {
        println("Example mod has been initialized.")
    }
}