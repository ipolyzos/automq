/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.kafka.server.config;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.security.scram.internals.ScramMechanism;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.kafka.common.config.ConfigDef.Importance.LOW;
import static org.apache.kafka.common.config.ConfigDef.Importance.MEDIUM;
import static org.apache.kafka.common.config.ConfigDef.Range.atLeast;
import static org.apache.kafka.common.config.ConfigDef.Type.CLASS;
import static org.apache.kafka.common.config.ConfigDef.Type.INT;

public class QuotaConfigs {
    public static final String NUM_QUOTA_SAMPLES_CONFIG = "quota.window.num";
    public static final String NUM_QUOTA_SAMPLES_DOC = "The number of samples to retain in memory for client quotas";
    public static final String NUM_CONTROLLER_QUOTA_SAMPLES_CONFIG = "controller.quota.window.num";
    public static final String NUM_CONTROLLER_QUOTA_SAMPLES_DOC = "The number of samples to retain in memory for controller mutation quotas";
    public static final String NUM_REPLICATION_QUOTA_SAMPLES_CONFIG = "replication.quota.window.num";
    public static final String NUM_REPLICATION_QUOTA_SAMPLES_DOC = "The number of samples to retain in memory for replication quotas";
    public static final String NUM_ALTER_LOG_DIRS_REPLICATION_QUOTA_SAMPLES_CONFIG = "alter.log.dirs.replication.quota.window.num";
    public static final String NUM_ALTER_LOG_DIRS_REPLICATION_QUOTA_SAMPLES_DOC = "The number of samples to retain in memory for alter log dirs replication quotas";

    // Always have 10 whole windows + 1 current window
    public static final int NUM_QUOTA_SAMPLES_DEFAULT = 11;

    public static final String QUOTA_WINDOW_SIZE_SECONDS_CONFIG = "quota.window.size.seconds";
    public static final String QUOTA_WINDOW_SIZE_SECONDS_DOC = "The time span of each sample for client quotas";
    public static final String CONTROLLER_QUOTA_WINDOW_SIZE_SECONDS_CONFIG = "controller.quota.window.size.seconds";
    public static final String CONTROLLER_QUOTA_WINDOW_SIZE_SECONDS_DOC = "The time span of each sample for controller mutations quotas";
    public static final String REPLICATION_QUOTA_WINDOW_SIZE_SECONDS_CONFIG = "replication.quota.window.size.seconds";
    public static final String REPLICATION_QUOTA_WINDOW_SIZE_SECONDS_DOC = "The time span of each sample for replication quotas";
    public static final String ALTER_LOG_DIRS_REPLICATION_QUOTA_WINDOW_SIZE_SECONDS_CONFIG = "alter.log.dirs.replication.quota.window.size.seconds";
    public static final String ALTER_LOG_DIRS_REPLICATION_QUOTA_WINDOW_SIZE_SECONDS_DOC = "The time span of each sample for alter log dirs replication quotas";
    public static final int QUOTA_WINDOW_SIZE_SECONDS_DEFAULT = 1;

    public static final String CLIENT_QUOTA_CALLBACK_CLASS_CONFIG = "client.quota.callback.class";
    public static final String CLIENT_QUOTA_CALLBACK_CLASS_DOC = "The fully qualified name of a class that implements the ClientQuotaCallback interface, " +
            "which is used to determine quota limits applied to client requests. By default, the &lt;user&gt; and &lt;client-id&gt; " +
            "quotas that are stored in ZooKeeper are applied. For any given request, the most specific quota that matches the user principal " +
            "of the session and the client-id of the request is applied.";

    public static final String LEADER_REPLICATION_THROTTLED_REPLICAS_CONFIG = "leader.replication.throttled.replicas";
    public static final String LEADER_REPLICATION_THROTTLED_REPLICAS_DOC = "A list of replicas for which log replication should be throttled on " +
            "the leader side. The list should describe a set of replicas in the form " +
            "[PartitionId]:[BrokerId],[PartitionId]:[BrokerId]:... or alternatively the wildcard '*' can be used to throttle " +
            "all replicas for this topic.";
    public static final List<String> LEADER_REPLICATION_THROTTLED_REPLICAS_DEFAULT = Collections.emptyList();

    public static final String FOLLOWER_REPLICATION_THROTTLED_REPLICAS_CONFIG = "follower.replication.throttled.replicas";
    public static final String FOLLOWER_REPLICATION_THROTTLED_REPLICAS_DOC = "A list of replicas for which log replication should be throttled on " +
            "the follower side. The list should describe a set of " + "replicas in the form " +
            "[PartitionId]:[BrokerId],[PartitionId]:[BrokerId]:... or alternatively the wildcard '*' can be used to throttle " +
            "all replicas for this topic.";
    public static final List<String> FOLLOWER_REPLICATION_THROTTLED_REPLICAS_DEFAULT = Collections.emptyList();


    public static final String LEADER_REPLICATION_THROTTLED_RATE_CONFIG = "leader.replication.throttled.rate";
    public static final String LEADER_REPLICATION_THROTTLED_RATE_DOC = "A long representing the upper bound (bytes/sec) on replication traffic for leaders enumerated in the " +
            String.format("property %s (for each topic). This property can be only set dynamically. It is suggested that the ", LEADER_REPLICATION_THROTTLED_REPLICAS_CONFIG) +
            "limit be kept above 1MB/s for accurate behaviour.";

    public static final String FOLLOWER_REPLICATION_THROTTLED_RATE_CONFIG = "follower.replication.throttled.rate";
    public static final String FOLLOWER_REPLICATION_THROTTLED_RATE_DOC = "A long representing the upper bound (bytes/sec) on replication traffic for followers enumerated in the " +
            String.format("property %s (for each topic). This property can be only set dynamically. It is suggested that the ", FOLLOWER_REPLICATION_THROTTLED_REPLICAS_CONFIG) +
            "limit be kept above 1MB/s for accurate behaviour.";
    public static final String REPLICA_ALTER_LOG_DIRS_IO_MAX_BYTES_PER_SECOND_CONFIG = "replica.alter.log.dirs.io.max.bytes.per.second";
    public static final String REPLICA_ALTER_LOG_DIRS_IO_MAX_BYTES_PER_SECOND_DOC = "A long representing the upper bound (bytes/sec) on disk IO used for moving replica between log directories on the same broker. " +
            "This property can be only set dynamically. It is suggested that the limit be kept above 1MB/s for accurate behaviour.";
    public static final long QUOTA_BYTES_PER_SECOND_DEFAULT = Long.MAX_VALUE;

    public static final String PRODUCER_BYTE_RATE_OVERRIDE_CONFIG = "producer_byte_rate";
    public static final String CONSUMER_BYTE_RATE_OVERRIDE_CONFIG = "consumer_byte_rate";
    public static final String REQUEST_PERCENTAGE_OVERRIDE_CONFIG = "request_percentage";
    public static final String CONTROLLER_MUTATION_RATE_OVERRIDE_CONFIG = "controller_mutation_rate";
    public static final String IP_CONNECTION_RATE_OVERRIDE_CONFIG = "connection_creation_rate";
    public static final String PRODUCER_BYTE_RATE_DOC = "A rate representing the upper bound (bytes/sec) for producer traffic.";
    public static final String CONSUMER_BYTE_RATE_DOC = "A rate representing the upper bound (bytes/sec) for consumer traffic.";
    public static final String REQUEST_PERCENTAGE_DOC = "A percentage representing the upper bound of time spent for processing requests.";
    public static final String CONTROLLER_MUTATION_RATE_DOC = "The rate at which mutations are accepted for the create " +
            "topics request, the create partitions request and the delete topics request. The rate is accumulated by " +
            "the number of partitions created or deleted.";
    public static final String IP_CONNECTION_RATE_DOC = "An int representing the upper bound of connections accepted " +
            "for the specified IP.";

    // AutoMQ inject start
    /**
     * All clients created by AutoMQ will have this prefix in their client id, and they will be excluded from quota.
     */
    public static final String INTERNAL_CLIENT_ID_PREFIX = "__automq_client_";

    public static final String BROKER_QUOTA_ENABLED_CONFIG = "broker.quota.enabled";
    public static final String BROKER_QUOTA_PRODUCE_BYTES_CONFIG = "broker.quota.produce.bytes";
    public static final String BROKER_QUOTA_FETCH_BYTES_CONFIG = "broker.quota.fetch.bytes";
    public static final String BROKER_QUOTA_SLOW_FETCH_BYTES_CONFIG = "broker.quota.slow.fetch.bytes";
    public static final String BROKER_QUOTA_REQUEST_RATE_CONFIG = "broker.quota.request.rate";
    public static final String BROKER_QUOTA_WHITE_LIST_USER_CONFIG = "broker.quota.white.list.user";
    public static final String BROKER_QUOTA_WHITE_LIST_CLIENT_ID_CONFIG = "broker.quota.white.list.client.id";
    public static final String BROKER_QUOTA_WHITE_LIST_LISTENER_CONFIG = "broker.quota.white.list.listener";
    public static final String CLUSTER_QUOTA_TOPIC_COUNT_CONFIG = "cluster.quota.topic.count";
    public static final String CLUSTER_QUOTA_PARTITION_COUNT_CONFIG = "cluster.quota.partition.count";

    public static final String BROKER_QUOTA_ENABLED_DOC = "Enable broker quota.";
    public static final String BROKER_QUOTA_PRODUCE_BYTES_DOC = "The maximum bytes send by producer in single window.";
    public static final String BROKER_QUOTA_FETCH_BYTES_DOC = "The maximum bytes receive by consumer in single window.";
    public static final String BROKER_QUOTA_SLOW_FETCH_BYTES_DOC = "The maximum bytes receive by slow fetch consumer in single window.";
    public static final String BROKER_QUOTA_REQUEST_RATE_DOC = "The maximum request count send by client in single window.";
    public static final String BROKER_QUOTA_WHITE_LIST_USER_DOC = "Broker quota white list for user.";
    public static final String BROKER_QUOTA_WHITE_LIST_CLIENT_ID_DOC = "Broker quota white list for client id.";
    public static final String BROKER_QUOTA_WHITE_LIST_LISTENER_DOC = "Broker quota white list for listener name.";
    public static final String CLUSTER_QUOTA_TOPIC_COUNT_DOC = "The maximum topic count in cluster.";
    public static final String CLUSTER_QUOTA_PARTITION_COUNT_DOC = "The maximum partition count in cluster.";
    // AutoMQ inject end

    public static final int IP_CONNECTION_RATE_DEFAULT = Integer.MAX_VALUE;

    public static final ConfigDef CONFIG_DEF =  new ConfigDef()
            .define(QuotaConfigs.NUM_QUOTA_SAMPLES_CONFIG, INT, QuotaConfigs.NUM_QUOTA_SAMPLES_DEFAULT, atLeast(1), LOW, QuotaConfigs.NUM_QUOTA_SAMPLES_DOC)
            .define(QuotaConfigs.NUM_REPLICATION_QUOTA_SAMPLES_CONFIG, INT, QuotaConfigs.NUM_QUOTA_SAMPLES_DEFAULT, atLeast(1), LOW, QuotaConfigs.NUM_REPLICATION_QUOTA_SAMPLES_DOC)
            .define(QuotaConfigs.NUM_ALTER_LOG_DIRS_REPLICATION_QUOTA_SAMPLES_CONFIG, INT, QuotaConfigs.NUM_QUOTA_SAMPLES_DEFAULT, atLeast(1), LOW, QuotaConfigs.NUM_ALTER_LOG_DIRS_REPLICATION_QUOTA_SAMPLES_DOC)
            .define(QuotaConfigs.NUM_CONTROLLER_QUOTA_SAMPLES_CONFIG, INT, QuotaConfigs.NUM_QUOTA_SAMPLES_DEFAULT, atLeast(1), LOW, QuotaConfigs.NUM_CONTROLLER_QUOTA_SAMPLES_DOC)
            .define(QuotaConfigs.QUOTA_WINDOW_SIZE_SECONDS_CONFIG, INT, QuotaConfigs.QUOTA_WINDOW_SIZE_SECONDS_DEFAULT, atLeast(1), LOW, QuotaConfigs.QUOTA_WINDOW_SIZE_SECONDS_DOC)
            .define(QuotaConfigs.REPLICATION_QUOTA_WINDOW_SIZE_SECONDS_CONFIG, INT, QuotaConfigs.QUOTA_WINDOW_SIZE_SECONDS_DEFAULT, atLeast(1), LOW, QuotaConfigs.REPLICATION_QUOTA_WINDOW_SIZE_SECONDS_DOC)
            .define(QuotaConfigs.ALTER_LOG_DIRS_REPLICATION_QUOTA_WINDOW_SIZE_SECONDS_CONFIG, INT, QuotaConfigs.QUOTA_WINDOW_SIZE_SECONDS_DEFAULT, atLeast(1), LOW, QuotaConfigs.ALTER_LOG_DIRS_REPLICATION_QUOTA_WINDOW_SIZE_SECONDS_DOC)
            .define(QuotaConfigs.CONTROLLER_QUOTA_WINDOW_SIZE_SECONDS_CONFIG, INT, QuotaConfigs.QUOTA_WINDOW_SIZE_SECONDS_DEFAULT, atLeast(1), LOW, QuotaConfigs.CONTROLLER_QUOTA_WINDOW_SIZE_SECONDS_DOC)
            .define(QuotaConfigs.CLIENT_QUOTA_CALLBACK_CLASS_CONFIG, CLASS, null, LOW, QuotaConfigs.CLIENT_QUOTA_CALLBACK_CLASS_DOC);
    private static final Set<String> USER_AND_CLIENT_QUOTA_NAMES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            PRODUCER_BYTE_RATE_OVERRIDE_CONFIG,
            CONSUMER_BYTE_RATE_OVERRIDE_CONFIG,
            REQUEST_PERCENTAGE_OVERRIDE_CONFIG,
            CONTROLLER_MUTATION_RATE_OVERRIDE_CONFIG
    )));

    private static void buildUserClientQuotaConfigDef(ConfigDef configDef) {
        configDef.define(PRODUCER_BYTE_RATE_OVERRIDE_CONFIG, ConfigDef.Type.LONG, Long.MAX_VALUE,
                MEDIUM, PRODUCER_BYTE_RATE_DOC);

        configDef.define(CONSUMER_BYTE_RATE_OVERRIDE_CONFIG, ConfigDef.Type.LONG, Long.MAX_VALUE,
                MEDIUM, CONSUMER_BYTE_RATE_DOC);

        configDef.define(REQUEST_PERCENTAGE_OVERRIDE_CONFIG, ConfigDef.Type.DOUBLE,
                Integer.valueOf(Integer.MAX_VALUE).doubleValue(),
                MEDIUM, REQUEST_PERCENTAGE_DOC);

        configDef.define(CONTROLLER_MUTATION_RATE_OVERRIDE_CONFIG, ConfigDef.Type.DOUBLE,
                Integer.valueOf(Integer.MAX_VALUE).doubleValue(),
                MEDIUM, CONTROLLER_MUTATION_RATE_DOC);
    }

    public static ConfigDef brokerQuotaConfigs() {
        return new ConfigDef()
                // Round minimum value down, to make it easier for users.
                .define(QuotaConfigs.LEADER_REPLICATION_THROTTLED_RATE_CONFIG, ConfigDef.Type.LONG,
                        QuotaConfigs.QUOTA_BYTES_PER_SECOND_DEFAULT, ConfigDef.Range.atLeast(0),
                        MEDIUM, QuotaConfigs.LEADER_REPLICATION_THROTTLED_RATE_DOC)
                .define(QuotaConfigs.FOLLOWER_REPLICATION_THROTTLED_RATE_CONFIG, ConfigDef.Type.LONG,
                        QuotaConfigs.QUOTA_BYTES_PER_SECOND_DEFAULT, ConfigDef.Range.atLeast(0),
                        MEDIUM, QuotaConfigs.FOLLOWER_REPLICATION_THROTTLED_RATE_DOC)
                .define(QuotaConfigs.REPLICA_ALTER_LOG_DIRS_IO_MAX_BYTES_PER_SECOND_CONFIG, ConfigDef.Type.LONG,
                        QuotaConfigs.QUOTA_BYTES_PER_SECOND_DEFAULT, ConfigDef.Range.atLeast(0),
                        MEDIUM, QuotaConfigs.REPLICA_ALTER_LOG_DIRS_IO_MAX_BYTES_PER_SECOND_DOC)
                 // AutoMQ inject start
                .define(QuotaConfigs.BROKER_QUOTA_ENABLED_CONFIG, ConfigDef.Type.BOOLEAN, false, MEDIUM, QuotaConfigs.BROKER_QUOTA_ENABLED_DOC)
                .define(QuotaConfigs.BROKER_QUOTA_PRODUCE_BYTES_CONFIG, ConfigDef.Type.DOUBLE, Double.MAX_VALUE, MEDIUM, QuotaConfigs.BROKER_QUOTA_PRODUCE_BYTES_DOC)
                .define(QuotaConfigs.BROKER_QUOTA_FETCH_BYTES_CONFIG, ConfigDef.Type.DOUBLE, Double.MAX_VALUE, MEDIUM, QuotaConfigs.BROKER_QUOTA_FETCH_BYTES_DOC)
                .define(QuotaConfigs.BROKER_QUOTA_SLOW_FETCH_BYTES_CONFIG, ConfigDef.Type.DOUBLE, Double.MAX_VALUE, MEDIUM, QuotaConfigs.BROKER_QUOTA_SLOW_FETCH_BYTES_DOC)
                .define(QuotaConfigs.BROKER_QUOTA_REQUEST_RATE_CONFIG, ConfigDef.Type.DOUBLE, Double.MAX_VALUE, MEDIUM, QuotaConfigs.BROKER_QUOTA_REQUEST_RATE_DOC)
                .define(QuotaConfigs.BROKER_QUOTA_WHITE_LIST_USER_CONFIG, ConfigDef.Type.STRING, "", MEDIUM, QuotaConfigs.BROKER_QUOTA_WHITE_LIST_USER_DOC)
                .define(QuotaConfigs.BROKER_QUOTA_WHITE_LIST_CLIENT_ID_CONFIG, ConfigDef.Type.STRING, "", MEDIUM, QuotaConfigs.BROKER_QUOTA_WHITE_LIST_CLIENT_ID_DOC)
                .define(QuotaConfigs.BROKER_QUOTA_WHITE_LIST_LISTENER_CONFIG, ConfigDef.Type.STRING, "", MEDIUM, QuotaConfigs.BROKER_QUOTA_WHITE_LIST_LISTENER_DOC)
                .define(QuotaConfigs.CLUSTER_QUOTA_TOPIC_COUNT_CONFIG, ConfigDef.Type.LONG, Long.MAX_VALUE, MEDIUM, QuotaConfigs.CLUSTER_QUOTA_TOPIC_COUNT_DOC)
                .define(QuotaConfigs.CLUSTER_QUOTA_PARTITION_COUNT_CONFIG, ConfigDef.Type.LONG, Long.MAX_VALUE, MEDIUM, QuotaConfigs.CLUSTER_QUOTA_PARTITION_COUNT_DOC);
                 // AutoMQ inject start
    }

    public static ConfigDef userAndClientQuotaConfigs() {
        ConfigDef configDef = new ConfigDef();
        buildUserClientQuotaConfigDef(configDef);
        return configDef;
    }

    public static ConfigDef scramMechanismsPlusUserAndClientQuotaConfigs() {
        ConfigDef configDef = new ConfigDef();
        ScramMechanism.mechanismNames().forEach(mechanismName -> {
            configDef.define(mechanismName, ConfigDef.Type.STRING, null, MEDIUM,
                    "User credentials for SCRAM mechanism " + mechanismName);
        });
        buildUserClientQuotaConfigDef(configDef);
        return configDef;
    }

    public static ConfigDef ipConfigs() {
        ConfigDef configDef = new ConfigDef();
        configDef.define(IP_CONNECTION_RATE_OVERRIDE_CONFIG, ConfigDef.Type.INT, Integer.MAX_VALUE,
                ConfigDef.Range.atLeast(0), MEDIUM, IP_CONNECTION_RATE_DOC);
        return configDef;
    }

    public static Boolean isClientOrUserQuotaConfig(String name) {
        return USER_AND_CLIENT_QUOTA_NAMES.contains(name);
    }
}
