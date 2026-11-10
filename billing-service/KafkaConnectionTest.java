import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;

public class KafkaConnectionTest {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9094");
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "10000");
        props.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, "15000");

        try (Admin admin = Admin.create(props)) {
            System.out.println("Connecting to Kafka...");
            System.out.println("Cluster ID: " +
                admin.describeCluster().clusterId().get(15, TimeUnit.SECONDS));
            System.out.println("Brokers: " +
                admin.describeCluster().nodes().get(15, TimeUnit.SECONDS));
            System.out.println("Topics: " +
                admin.listTopics().names().get(15, TimeUnit.SECONDS));
            System.out.println("SUCCESS: Windows Kafka connection works.");
        }
    }
}
