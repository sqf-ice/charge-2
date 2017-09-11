package com.clouyun.charge.modules.document;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

/**
 * @author:zhangli
 * @desc:
 * <p></p>
 */
public class ReadXml {

	public static void main(String[] args) {
//		StringReader read = new StringReader(xmlDoc);
		File f = new File("E:/keys.xml");
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        try {
            //通过输入源构造一个Document
            Document doc = sb.build(new FileInputStream(f));
            //取的根元素
            Element root = doc.getRootElement();
            List<?> childs = root.getChildren("company");
            for(Object obj : childs){
            	String sql = "INSERT INTO card_keys (org_id,key1,key2,key3,key4,key5,key6,key7,key8,key9,key10,key11,key12,key13,key14,key15,key16) VALUES ( ";
            	Element e = (Element) obj;
            	String orgId = e.getAttributeValue("orgId");
            	sql+=orgId;
            	List<?> keys = e.getChildren();
            	for(Object o : keys){
            		e = (Element) o;
            		
            		String keyId = e.getAttributeValue("name");
            		String value = e.getText();
            		sql +=",'"+value+"'";
            	}
            	sql += " );";
            	System.out.println(sql);
            }
            
//            
//            //得到根元素所有子元素的集合
//            List jiedian = root.getChildren();
//            //获得XML中的命名空间（XML中未定义可不写）
//            Namespace ns = root.getNamespace();
//            Element et = null;
//            for(int i=0;i<jiedian.size();i++){
//                et = (Element) jiedian.get(i);//循环依次得到子元素
//               
//                System.out.println(et.getChild("users_id",ns).getText());
//                System.out.println(et.getChild("users_address",ns).getText());
//            }
//           
//            et = (Element) jiedian.get(0);
//            List zjiedian = et.getChildren();
//            for(int j=0;j<zjiedian.size();j++){
//                Element xet = (Element) zjiedian.get(j);
//                System.out.println(xet.getName());
//            }
        } catch (JDOMException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
	}

}
