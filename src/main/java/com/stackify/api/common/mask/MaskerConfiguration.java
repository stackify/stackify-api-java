package com.stackify.api.common.mask;


import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Properties;

/**
 * Handled properties:
 * <p>
 * stackify.log.mask.enabled=true|false
 * stackify.log.mask.[CREDITCARD|SSN|IP]=true|false
 * stackify.log.mask.custom.[CUSTOM_LABEL]=[regex]
 *
 * @author Darin Howard
 */
@Slf4j
public class MaskerConfiguration {

    public static Masker fromProperties() {

        Masker masker = new Masker();

        // set default enabled masks
        masker.addMask(Masker.MASK_CREDITCARD);
        masker.addMask(Masker.MASK_SSN);

        FileReader confFileReader = null;

        try {
            URL confFileUrl = MaskerConfiguration.class.getResource("/stackify-api.properties");

            if (confFileUrl != null) {
                File confFile = new File(confFileUrl.toURI());

                if (confFile.exists()) {

                    confFileReader = new FileReader(confFile);

                    Properties confProps = new Properties();
                    confProps.load(confFileReader);

                    if (confProps.containsKey("stackify.log.mask.enabled")) {
                        if (!Boolean.parseBoolean(confProps.getProperty("stackify.log.mask.enabled"))) {
                            masker.clearMasks();
                            return masker;
                        }
                    }

                    // set masks
                    for (String builtinMask : Masker.MASKS) {
                        if (confProps.contains("stackify.log.mask." + builtinMask)) {
                            if (Boolean.parseBoolean(confProps.getProperty("stackify.log.mask." + builtinMask))) {
                                masker.addMask(builtinMask);
                            } else {
                                masker.removeMask(builtinMask);
                            }
                        }
                    }

                    // set custom masks
                    for (Object key : confProps.keySet()) {
                        if (key.toString().startsWith("stackify.log.mask.custom.")) {
                            masker.addMask(confProps.getProperty(key.toString()));
                        }
                    }

                }
            }

        } catch (Throwable t) {
            log.error("Exception reading stackify-api.properties configuration file", t);
        } finally {
            if (confFileReader != null) {
                try {
                    confFileReader.close();
                } catch (Throwable t) {
                    log.info("Exception closing stackify-api.properties configuration file", t);
                }
            }
        }

        return masker;
    }

}
