/*
 * Copyright sablintolya@gmai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ma1uta.saimaa;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.ma1uta.saimaa.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * AppService.
 */
@CommandLine.Command(name = "java -jar saimaa.jar")
public class AppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loggers.LOGGER);

    @CommandLine.Option(names = {"-f", "--file"}, description = "configuration file", defaultValue = "saimaa.yml")
    private String configFile = "saimaa.yml";

    @CommandLine.Option(names = {"-c", "--config"}, description = "configuration files location")
    private String configLocation;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "display a help message")
    private boolean helpRequested = false;

    /**
     * Main entry point.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        CommandLine.populateCommand(new AppService(), args).run();
    }

    /**
     * Run AppService.
     */
    public void run() {
        try {
            if (helpRequested) {
                CommandLine.usage(this, System.out);
                return;
            }

            Path configPath;
            if (configLocation != null) {
                configPath = Paths.get(configLocation, configFile);
            } else {
                configPath = Paths.get(configFile);
            }

            ObjectMapper configMapper = new ObjectMapper(new YAMLFactory());
            configMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            AppConfig appConfig = configMapper.readValue(configPath.toFile(), AppConfig.class);

            new Bridge().run(appConfig);
        } catch (Exception e) {
            LOGGER.error("Failed run the bridge.", e);
        }
    }
}
