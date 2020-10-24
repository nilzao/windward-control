package io.github.nilzao.windwardcontrol.key;

import com.pi4j.io.gpio.*;
import io.github.nilzao.windwardcontrol.gpio.GpioImpl;
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
                GpioImpl gpio = GpioImpl.getInstance();
                gpio.pinoutOneHigh();
                Thread.sleep(2000l);
                gpio.pinoutOneLow();
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
