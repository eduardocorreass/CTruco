import com.bonelli.noli.paulistabot.PaulistaBot;
import com.bueno.impl.dummybot.DummyBot;
import com.caueisa.destroyerbot.DestroyerBot;
import com.cremonezzi.impl.carlsenbot.Carlsen;
import com.hermespiassi.casados.marrecobot.MarrecoBot;
import com.hideki.araujo.wrkncacnterbot.WrkncacnterBot;
import com.indi.impl.addthenewsoul.AddTheNewSoul;
import com.correa.carini.impl.trucomachinebot.TrucoMachineBot;

module bot.impl {
    requires bot.spi;
    exports com.bueno.impl.dummybot;
    exports com.indi.impl.addthenewsoul;
    exports com.hermespiassi.casados.marrecobot;
    exports com.cremonezzi.impl.carlsenbot;
    exports com.caueisa.destroyerbot;
    exports com.bonelli.noli.paulistabot;
    exports com.hideki.araujo.wrkncacnterbot;
    provides com.bueno.spi.service.BotServiceProvider with
            DummyBot, Carlsen, DestroyerBot, WrkncacnterBot, PaulistaBot, MarrecoBot, AddTheNewSoul, TrucoMachineBot;
}