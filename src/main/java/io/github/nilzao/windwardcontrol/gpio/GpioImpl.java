package io.github.nilzao.windwardcontrol.gpio;

import com.pi4j.io.gpio.*;

public class GpioImpl {

    private static GpioImpl instance;

    private final GpioController gpio;
    private final GpioPinDigitalOutput pinOut;


    private GpioImpl() {
        gpio = GpioFactory.getInstance();
        pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Gate", PinState.LOW);
        pinOut.setShutdownOptions(true, PinState.LOW);
    }

    public static GpioImpl getInstance() {
        if (instance == null) {
            instance = new GpioImpl();
        }
        return instance;
    }

    public void pinoutOneHigh() {
        pinOut.high();
    }

    public void pinoutOneLow() {
        pinOut.low();
    }

}
