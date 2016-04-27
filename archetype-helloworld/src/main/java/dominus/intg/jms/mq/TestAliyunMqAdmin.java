package dominus.intg.jms.mq;


import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.FormatType;
//EE: public cloud & finance cloud package
import com.aliyuncs.ons4financehz.model.v20160405.*;
//import com.aliyuncs.ons.model.v20160405.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 注意：因为POP网关是面对公网环境提供服务的，因此使用Open-API的前提是，客户端需要能够访问公网服务。否则会提示服务无法连接
 */
public class TestAliyunMqAdmin extends TestAliyunMqZBaseTestCase {

    String currentTopic;

    @Override
    protected void doSetUp() throws Exception {
        super.doSetUp();
        this.currentTopic = testTopicId;
    }


    @Test
    public void testOnsTopicStatusRequest() throws ClientException {

        currentTopic = TEST_10K_QUEUE;

        //template code begin
        OnsTopicStatusRequest request = new OnsTopicStatusRequest();
        request.setAcceptFormat(FormatType.JSON);
        request.setOnsRegionId(ONS_REGION_ID);
        request.setPreventCache(System.currentTimeMillis());
        request.setTopic(currentTopic);
        OnsTopicStatusResponse response = iAcsClient.getAcsResponse(request);
        OnsTopicStatusResponse.Data data = response.getData();
        assertNotNull(data);
        //template code end
        System.out.printf("[%s] totalCount %d lastTimeStamp %d\n", currentTopic, data.getTotalCount(), data.getLastTimeStamp());
    }


    @Test
    public void testOnsTopicGetRequest() throws ClientException {

        currentTopic = TEST_10K_QUEUE;

        OnsTopicGetRequest request = new OnsTopicGetRequest();
        request.setAcceptFormat(FormatType.JSON);
        request.setTopic(currentTopic);
        request.setOnsRegionId(ONS_REGION_ID);
        request.setPreventCache(System.currentTimeMillis());

        OnsTopicGetResponse response = iAcsClient.getAcsResponse(request);
        List<OnsTopicGetResponse.PublishInfoDo> publishInfoDoList = response.getData();
        for (OnsTopicGetResponse.PublishInfoDo publishInfoDo : publishInfoDoList) {
            System.out.println(publishInfoDo.getId() + "  " +
                    publishInfoDo.getChannelId() + "  " +
                    publishInfoDo.getChannelName() + "  " +
                    publishInfoDo.getOnsRegionId() + "  " +
                    publishInfoDo.getRegionName() + "  " +
                    publishInfoDo.getTopic() + "  " +
                    publishInfoDo.getOwner() + "  " +
                    publishInfoDo.getRelation() + "  " +
                    publishInfoDo.getRelationName() + "  " +
                    publishInfoDo.getStatus() + "  " +
                    publishInfoDo.getStatusName() + "  " +
                    publishInfoDo.getAppkey() + "  " +
                    publishInfoDo.getCreateTime() + "  " +
                    publishInfoDo.getUpdateTime() + "  " +
                    publishInfoDo.getRemark());
        }
        System.out.println(response.getRequestId());


    }


    /**
     * 1  cn-qingdao-publictest  公网测试  null    1411623866000  1411623866000
     * 3  cn-qingdao  华北 1 (青岛)  null    1411623866000  1411623866000
     * 4  cn-shenzhen  华南 1 (深圳)  null    1411623866000  1411623866000
     * 6  cn-hangzhou  华东 1 (杭州)  null    1413877180000  1413877180000
     * 8  cn-beijing  华北 2 (北京)  null    1413008259000  1413008259000
     * 9  cn-shanghai  华东 2 (上海)  null    1452840680000  1452840688000
     * <p/>
     * EE:financial private cloud
     * 1  finance-cn-hangzhou  华东 1 (杭州)  null    1417934380000  1417934380000
     * 2  finance-cn-shenzhen  华南 1 (深圳)  null    1456497165000  1456497165000
     *
     * @throws ClientException
     */
    @Test
    public void testOnsRegionListRequest() throws ClientException {
        OnsRegionListRequest request = new OnsRegionListRequest();
        request.setOnsRegionId(ONS_REGION_ID);
        request.setAcceptFormat(FormatType.JSON);
        request.setPreventCache(System.currentTimeMillis());

        OnsRegionListResponse response = iAcsClient.getAcsResponse(request);
        List<OnsRegionListResponse.RegionDo> regionDoList = response.getData();
        for (OnsRegionListResponse.RegionDo regionDo : regionDoList) {
            System.out.println(regionDo.getId() + "  " +
                    regionDo.getOnsRegionId() + "  " +
                    regionDo.getRegionName() + "  " +
                    regionDo.getChannelId() + "  " +
                    regionDo.getChannelName() + "  " +
                    regionDo.getCreateTime() + "  " +
                    regionDo.getUpdateTime());
        }
    }

    @Test
    public void testListMyTopic() throws ClientException {
        OnsTopicListRequest request = new OnsTopicListRequest();
        request.setOnsRegionId(ONS_REGION_ID);
        request.setPreventCache(System.currentTimeMillis());

        OnsTopicListResponse response = iAcsClient.getAcsResponse(request);
        List<OnsTopicListResponse.PublishInfoDo> publishInfoDoList = response.getData();
        for (OnsTopicListResponse.PublishInfoDo publishInfoDo : publishInfoDoList) {
            if (publishInfoDo.getTopic().contains("GUOXU"))
                System.out.println(publishInfoDo.getTopic() + "     " + publishInfoDo.getOwner());
        }
    }

    @Test
    public void testOnsTopicCreateRequest() throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile(ACS_REGION_ID, accessKey, secretKey);
        IAcsClient client = new DefaultAcsClient(profile);

        OnsTopicCreateRequest request = new OnsTopicCreateRequest();
        request.setAcceptFormat(FormatType.JSON);
        request.setTopic("D-GUOXU-TEST-160418");
        request.setQps(1000l);
        request.setRemark("DEV");
        request.setStatus(0);
        /**
         *ONSRegionId是指你需要API访问ONS哪个区域的资源。
         *该值必须要根据OnsRegionList方法获取的列表来选择和配置，因为OnsRegionId是变动的，不能够写固定值
         */
        request.setOnsRegionId(ONS_REGION_ID);
        request.setCluster("DEV-Test");
        request.setPreventCache(System.currentTimeMillis());

        OnsTopicCreateResponse response = client.getAcsResponse(request);
        System.out.println(response.getRequestId());
    }

    @Test
    public void testOnsTopicDeleteRequest() throws ClientException {

        String deleteTopic = "D-GUOXU-TEST-100K";

        IClientProfile profile = DefaultProfile.getProfile(ACS_REGION_ID, accessKey, secretKey);
        IAcsClient client = new DefaultAcsClient(profile);

        OnsTopicDeleteRequest request = new OnsTopicDeleteRequest();
        request.setPreventCache(System.currentTimeMillis());
        request.setOnsRegionId(ONS_REGION_ID);
        request.setTopic(deleteTopic);

        OnsTopicDeleteResponse response = client.getAcsResponse(request);
        System.out.printf("%s is deleted! %s\n", deleteTopic, response.getRequestId());
    }

    @Test
    public void testOnsTrendTopicInputTpsRequest() throws ClientException {

        String currentTopic = TEST_10K_QUEUE;

        OnsTrendTopicInputTpsRequest request = new OnsTrendTopicInputTpsRequest();
        request.setOnsRegionId(ONS_REGION_ID);
        request.setPreventCache(System.currentTimeMillis());
        request.setAcceptFormat(FormatType.JSON);
        request.setTopic(currentTopic);
        request.setBeginTime(System.currentTimeMillis() - 120 * Minute);
        request.setEndTime(System.currentTimeMillis());
        request.setPeriod(1L);
        request.setType(1);

        OnsTrendTopicInputTpsResponse response = iAcsClient.getAcsResponse(request);
        OnsTrendTopicInputTpsResponse.Data data = response.getData();
        System.out.println(data.getTitle());
        for (OnsTrendTopicInputTpsResponse.Data.StatsDataDo stat : data.getRecords()) {
            out.printf("X: %s, Y: %s\n", stat.getX(), stat.getY());
        }
    }

    public static final String TEST_10K_QUEUE = "D-GUOXU-TEST-10K";
    public static final String TEST_100K_QUEUE = "D-GUOXU-TEST-100K";
    public static final String TEST_ONE_MSG_QUEUE = "D-GUOXU-TEST-ONE";

    @Test
    public void create100KQueue() throws ClientException, IllegalAccessException, InterruptedException {
        String publishId = "PID-" + TEST_100K_QUEUE;
        this.createTestTopic(TEST_100K_QUEUE);
        this.createProducerPublish(TEST_100K_QUEUE, publishId);
        Producer producer = this.createProducer(publishId);
        produceTestMessage(producer, TEST_100K_QUEUE, 100000L);
        this.deleteProducerPublish(TEST_10K_QUEUE, "PID-D-GUOXU-TEST-10k");
    }

    @Test
    public void testCreateOneMsgQueue() throws ClientException, IllegalAccessException, InterruptedException {
        String publishId = "PID-" + TEST_ONE_MSG_QUEUE;
        this.createTestTopic(TEST_ONE_MSG_QUEUE);
        this.createProducerPublish(TEST_100K_QUEUE, publishId);
        Producer producer = this.createProducer(publishId);
        produceTestMessage(producer, TEST_ONE_MSG_QUEUE, 1L);
        this.deleteProducerPublish(TEST_ONE_MSG_QUEUE, publishId);
    }
}