/**
 * Copyright 2013-2014 TORCH GmbH, 2015 Graylog, Inc.
 *
 * This file is part of Graylog.
 *
 * Graylog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.graylog2.alarmcallbacks.hipchat;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackConfigurationException;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

public class HipChatAlarmCallbackTest {
    private HipChatAlarmCallback alarmCallback;

    @Before
    public void setUp() {
        alarmCallback = new HipChatAlarmCallback();
    }

    @Test
    public void testInitialize() throws AlarmCallbackConfigurationException {
        final Configuration configuration = new Configuration(ImmutableMap.<String, Object>of(
                "api_token", "TEST_api_token",
                "room", "TEST_room",
                "color", "yellow",
                "notify", true,
                "msg_template", "Stream template"
        ));
        alarmCallback.initialize(configuration);
    }

    @Test
    public void testGetAttributes() throws AlarmCallbackConfigurationException {
        final Configuration configuration = new Configuration(ImmutableMap.<String, Object>of(
                "api_token", "TEST_api_token",
                "room", "TEST_room",
                "color", "yellow",
                "notify", true,
                "msg_template", "Stream template"
        ));
        alarmCallback.initialize(configuration);

        final Map<String, Object> attributes = alarmCallback.getAttributes();
        assertThat(attributes.keySet(), hasItems("api_token", "room", "color", "notify", "msg_template"));
        assertThat((String) attributes.get("api_token"), equalTo("****"));
    }

    @Test
    public void checkConfigurationSucceedsWithValidConfiguration()
            throws AlarmCallbackConfigurationException, ConfigurationException {
        final Configuration configuration = new Configuration(ImmutableMap.<String, Object>of(
                "api_token", "TEST_api_token",
                "room", "TEST_room",
                "color", "yellow",                
                "notify", true,
                "msg_template", "Stream template" 

        ));
        alarmCallback.initialize(configuration);
        alarmCallback.checkConfiguration();
    }

    @Test(expected = ConfigurationException.class)
    public void checkConfigurationFailsIfApiTokenIsMissing()
            throws AlarmCallbackConfigurationException, ConfigurationException {
        final Configuration configuration = new Configuration(ImmutableMap.<String, Object>of(
                "room", "TEST_room"
        ));
        alarmCallback.initialize(configuration);
        alarmCallback.checkConfiguration();
    }

    @Test(expected = ConfigurationException.class)
    public void checkConfigurationFailsIfRoomIsMissing()
            throws AlarmCallbackConfigurationException, ConfigurationException {
        final Configuration configuration = new Configuration(ImmutableMap.<String, Object>of(
                "api_token", "TEST_api_token"
        ));
        alarmCallback.initialize(configuration);
        alarmCallback.checkConfiguration();
    }

    @Test(expected = ConfigurationException.class)
    public void checkConfigurationFailsIfMsgTemplateIsMissing()
            throws AlarmCallbackConfigurationException, ConfigurationException {
        final Configuration configuration = new Configuration(ImmutableMap.<String, Object>of(
                "api_token", "TEST_api_token",
                "room", "TEST_room",
                "color", "yellow",                
                "notify", true
        ));
        alarmCallback.initialize(configuration);
        alarmCallback.checkConfiguration();
    }

    @Test(expected = ConfigurationException.class)
    public void checkConfigurationFailsIfRoomIsTooLong()
            throws AlarmCallbackConfigurationException, ConfigurationException {
        final Configuration configuration = new Configuration(ImmutableMap.<String, Object>of(
                "api_token", "TEST_api_token",
                "room", Strings.repeat("a", 101)
        ));
        alarmCallback.initialize(configuration);
        alarmCallback.checkConfiguration();
    }

    @Test(expected = ConfigurationException.class)
    public void checkConfigurationFailsIfColorIsInvalid()
            throws AlarmCallbackConfigurationException, ConfigurationException {
        final Configuration configuration = new Configuration(ImmutableMap.<String, Object>of(
                "api_token", "TEST_api_token",
                "room", "TEST_room",
                "color", "INVALID"
        ));
        alarmCallback.initialize(configuration);
        alarmCallback.checkConfiguration();
    }

    @Test
    public void testGetRequestedConfiguration() {
        assertThat(alarmCallback.getRequestedConfiguration().asList().keySet(),
                hasItems("api_token", "room", "color", "notify", "msg_template"));
    }

    @Test
    public void testGetName() {
        assertThat(alarmCallback.getName(), equalTo("HipChat alarm callback"));
    }

}