package io.github.nilzao.windwardcontrol.key;

import com.pi4j.io.gpio.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
public class KeyController {

    private final KeyRepository keyRepository;

    @RequestMapping(value = "/greeting/{keyHash}", method = RequestMethod.GET)
    public String greeting(
            @PathVariable("keyHash") String keyHash,
            Model model) {
        if (fillModel(model, keyHash)) {
            return "greeting";
        }
        return "deny";
    }

    @RequestMapping(value = "/greeting/{keyHash}", method = RequestMethod.POST)
    public String openGate(@PathVariable String keyHash, Model model) {
        if (fillModel(model, keyHash)) {
            try {
                GpioController gpio = GpioFactory.getInstance();
                GpioPinDigitalOutput pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01,
                        "Gate",
                        PinState.LOW);
                pinOut.setShutdownOptions(true, PinState.LOW);
                pinOut.high();
                Thread.sleep(2000l);
                pinOut.low();
            } catch (Exception e) {
                e.printStackTrace();
            }
            model.addAttribute("command", "Opening the gate!");
            return "greeting";
        }
        return "deny";
    }

    private boolean fillModel(Model model, String keyHash) {
        Key keyEntity = keyRepository.findByKey(keyHash);
        if (keyEntity == null) {
            return false;
        }
        model.addAttribute("key", keyHash);
        model.addAttribute("owner", keyEntity.getOwner());
        return true;
    }
}
