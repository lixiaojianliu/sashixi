package test;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.common.utils.ServiceSettings;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.QueueMeta;

import java.sql.Struct;


public class mns {
    private CloudAccount account;
    private MNSClient client;
    public mns() {
         account = new CloudAccount(
                ServiceSettings.getMNSAccessKeyId(),
                ServiceSettings.getMNSAccessKeySecret(),
                ServiceSettings.getMNSAccountEndpoint());
        client = account.getMNSClient();
    }
    public String creatQuene(String quName)
    {
        if (!client.isOpen())
            client = account.getMNSClient();
        try {
            CloudQueue queue = client.getQueueRef(quName);
            if (queue.isQueueExist()) {
                return "Create failed! Because the queue " + quName + " is exist!\n";
            } else {
                QueueMeta queueMeta = new QueueMeta();
                queueMeta.setQueueName(quName);
                queueMeta.setPollingWaitSeconds(30);
                CloudQueue cloudQueue = client.createQueue(queueMeta);
                return "Create " + quName + " successfully. "+"\n"+"URL: " + cloudQueue.getQueueURL() + "\n";
            }

        } catch (ClientException ce) {
            ce.printStackTrace();
            return "Something wrong with the network connection between client and MNS service."
                    + "Please check your network and DNS availability.\n";
        } catch (ServiceException se) {
            se.printStackTrace();
            if (se.getErrorCode().equals("QueueNotExist")) {
                return "Queue is not exist.Please createQueue before use\n";
            } else if (se.getErrorCode().equals("TimeExpired")) {
                return "The request is time expired. Please check your local machine timeclock\n";
            }
            /*
            you can get more MNS service error code in following link.
            https://help.aliyun.com/document_detail/mns/api_reference/error_code/error_code.html?spm=5176.docmns/api_reference/error_code/error_response
            */
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown exception happened!\n";
        }
        client.close();
        return "";
    }
    public String deleteQuene(String Qname)
    {
        if (!client.isOpen())
            client = account.getMNSClient();
        try
        {   //Delete Queue
            CloudQueue queue = client.getQueueRef(Qname);
            if (queue.isQueueExist())
            {
                queue.delete();
            return "Delete "+Qname+" successfully!";
            }
            else
            {
                return "Delete " + Qname + " failed! Because the queue is not exist!\n";

            }
        } catch (ClientException ce)
        {
            ce.printStackTrace();
            return "Something wrong with the network connection between client and MNS service."
                    +"\n"+ "Please check your network and DNS availablity.";
        } catch (ServiceException se)
        {
            if (se.getErrorCode().equals("QueueNotExist"))
            {
                return "Queue is not exist.Please create before use";
            } else if (se.getErrorCode().equals("TimeExpired"))
            {
                return "The request is time expired. Please check your local machine timeclock";
            }
            /*
            you can get more MNS service error code in following link.
            https://help.aliyun.com/document_detail/mns/api_reference/error_code/error_code.html?spm=5176.docmns/api_reference/error_code/error_response
            */
            se.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
            return "Unknown exception happened!";
        }

        client.close();
        return "";
    }

    public String receiveMessage(String Qname)
    {
        if (!client.isOpen())
            client = account.getMNSClient();
        try{
            CloudQueue queue = client.getQueueRef(Qname);// replace with your queue name
            Message popMsg=queue.popMessage();
            /*if (popMsg != null){
                System.out.println("message handle: " + popMsg.getReceiptHandle());
                System.out.println("message body: " + popMsg.getMessageBodyAsString());
                System.out.println("message id: " + popMsg.getMessageId());
                System.out.println("message dequeue count:" + popMsg.getDequeueCount());
                //<<to add your special logic.>>

                //remember to  delete message when consume message successfully.
                queue.deleteMessage(popMsg.getReceiptHandle());
                System.out.println("delete message successfully.\n");
            }*/
            if (popMsg!=null){
                String handle=popMsg.getReceiptHandle();
                String body=popMsg.getMessageBodyAsString();
                String id=popMsg.getMessageId();
                int dequeueCount=popMsg.getDequeueCount();
                queue.deleteMessage(popMsg.getReceiptHandle());
                return "message handle: " + handle + "\n"
                        + "message body: " + body + "\n"
                        + "message id: " + id + "\n"
                        + "message dequeue count:" + dequeueCount + "\n";
            }

        }
        catch (ClientException ce)
        {
            ce.printStackTrace();
            return "Something wrong with the network connection between client and MNS service."
                    + "Please check your network and DNS availablity.";
        } catch (ServiceException se)
        {
            if (se.getErrorCode().equals("QueueNotExist"))
            {
                return "Queue is not exist.Please create queue before use";
            } else if (se.getErrorCode().equals("TimeExpired"))
            {
                return "The request is time expired. Please check your local machine timeclock";
            }
            /*
            you can get more MNS service error code in following link.
            https://help.aliyun.com/document_detail/mns/api_reference/error_code/error_code.html?spm=5176.docmns/api_reference/error_code/error_response
            */
            se.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
            return "Unknown exception happened!";
        }
        return "";
    }
    public String sendMessage(String Qname,String body){
        if(!client.isOpen()){
            client = account.getMNSClient();
        }
        try{
            CloudQueue queue = client.getQueueRef(Qname);// replace with your queue name

                Message message = new Message();
                message.setMessageBody(body); // use your own message body here
                Message putMsg = queue.putMessage(message);
                return "Send message id is: " + putMsg.getMessageId()+"\n";
                }
        catch (ClientException ce)
        {
            ce.printStackTrace();
            return "Something wrong with the network connection between client and MNS service."
                    + "Please check your network and DNS availablity.";
        } catch (ServiceException se)
        {
            if (se.getErrorCode().equals("QueueNotExist"))
            {
                return "Queue is not exist.Please create before use";
            } else if (se.getErrorCode().equals("TimeExpired"))
            {
                return "The request is time expired. Please check your local machine timeclock";
            }
            /*
            you can get more MNS service error code from following link:
            https://help.aliyun.com/document_detail/mns/api_reference/error_code/error_code.html?spm=5176.docmns/api_reference/error_code/error_response
            */
            se.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
            return "Unknown exception happened!";
        }

        client.close();
        return "";
    }
}
