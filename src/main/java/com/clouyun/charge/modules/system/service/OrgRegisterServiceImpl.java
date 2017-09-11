package com.clouyun.charge.modules.system.service;

import java.util.List;
import java.util.zip.CheckedInputStream;

import com.clou.entitys.data.data;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.helper.UserHelper;
import com.clouyun.charge.modules.system.mapper.UserMapper;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.charge.modules.system.mapper.ChgStationMapper;
import com.clouyun.charge.modules.system.mapper.PubOrgMapper;
import com.clouyun.charge.modules.system.mapper.PubUserMapper;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrgRegisterServiceImpl {
	
	@Autowired
	PubUserMapper pubUserMapper;
	

	@Autowired
	PubOrgMapper pubOrgMapper;
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	ChgStationMapper chgStationMapper;

	//查询企业管理员信息
	public	DataVo selectPubUser(DataVo dv){
		return pubUserMapper.selectPubUser(dv);
	}
	
	
	//根据userId查询企业管理员信息 
	public 	DataVo selectPubUserByuserId(DataVo dv){
		return pubUserMapper.selectPubUserByuserId(dv);
	}
	
	//根据userId更新企业管理员信息 
	public 	int updatePubUserByuserId(DataVo dv){
		return pubUserMapper.updatePubUserByuserId(dv);
	}
	
	//判断该用户名是否存在 
	public	List<DataVo> selectPubUserByuserName(DataVo dv){
		return pubUserMapper.selectPubUserByuserName(dv);
	}
	
	
	
	//新增场站信息
	public int insertChgStation(DataVo dv){
		return chgStationMapper.insertChgStation(dv);
		
	}
	
	 //新增场站的充电桩类型信息
	public int insertChgPileType(DataVo dv){
		return chgStationMapper.insertChgPileType(dv);
		
	}
	
	//根据orgId查询企业信息 
	public DataVo selectPubOrgByOrgId(DataVo dv){
		return pubOrgMapper.selectPubOrgByOrgId(dv);
		
	}
	
  	
   //根据orgId更新企业信息 
  	public 	int updatePubOrgByOrgId(DataVo dv){
  		return pubOrgMapper.updatePubOrgByOrgId(dv);
  	}
  	
  	
   //根据地区编码查询
  	public	List<DataVo> selectPubOrgByDistCode(DataVo dv){
  		return pubOrgMapper.selectPubOrgByDistCode(dv);
  	}
  	
  	// 企业审核查询 
  	public	List<DataVo> selectPubOrgAudit(DataVo dv){
  		return pubOrgMapper.selectPubOrgAudit(dv);
  	}
  	
    // 企业审核统计
   	public	Integer countAudit(DataVo dv){
   		return pubOrgMapper.countAudit(dv);
   	}
	
    
   // 企业未审核,审核通过，审核不通过的详细信息查询
   	public	List<DataVo> detailAudit(DataVo dv){
   		return pubOrgMapper.detailAudit(dv);
   	}
   	
   	// //查询企业场站信息
   	public	List<DataVo> detailAuditChg(DataVo dv){
   		return pubOrgMapper.detailAuditChg(dv);
   	}
   	
    //获取企业审核的审核人列表，查询企业表 
   	public	List<DataVo> queryAuditMember(DataVo dv){
   		return pubOrgMapper.queryAuditMember(dv);
   	}
   	
   	
    //新增模板 
    public int insertPubPrice(DataVo dv){
    	return chgStationMapper.insertPubPrice(dv);
    }
    
    //新增费率对应的时间段 
    public int insertModelTime(DataVo dv){
    	return chgStationMapper.insertModelTime(dv);
    }
    
    /**
     * 给企业管理员分配角色
     * @param dv
     * @return
     */
  	public int insertUserRole(DataVo dv){
  		return  pubUserMapper.insertUserRole(dv);
  	}
  	
  	public int insertPubUserOrgStation(DataVo dv){
  		return  pubUserMapper.insertPubUserOrgStation(dv);
  	}

   //根据stationId查询充电桩类型信息
  	public	List<DataVo> selectChgPileType(DataVo dv){
  		return chgStationMapper.selectChgPileType(dv);
  	}
	/**
	 * 企业用户注册
	 * @param vo
	 */
	@Transactional(rollbackFor=Exception.class)
    public void register(DataVo vo) throws BizException {
		chckRegister(vo);
		vo.add("orgType", "01");
		vo.add("paymentProcess","0");
		vo.add("orgAddr",vo.getString("provCode")+vo.getString("cityCode")+vo.getString("distCode"));
		//企业编号生成规则
		String toDistCode=toDistCode(vo.getString("distCode"),null);
		DataVo ddv=new DataVo();
		ddv.put("orgNo", toDistCode);
		pubOrgMapper.insertPubOrg(vo);
		String password = vo.getString("password");
		String rawSalt = UserHelper.buildRawSalt(vo);//构建原始盐
		String enPassword = UserHelper.encoderPassword(password, rawSalt);//MD5盐加密
		String enSalt = UserHelper.encodeSalt(rawSalt);//加密盐
		vo.put("salt", enSalt);
		vo.put("password", enPassword);
		vo.put("userState", 02);
		vo.put("userNo", "nouser");
	    userMapper.insertUser(vo);
		vo.put("userId",vo.getInt("id"));
		pubOrgMapper.updatePubOrgByOrgId(vo);
		chgStationMapper.insertChgStation(vo);
    }

	private void chckRegister(DataVo vo) throws BizException {
		String memberPhone = vo.getString("memberPhone");
		String orgEmail = vo.getString("orgEmail");
		String orgPhone = vo.getString("orgPhone");
		String email = vo.getString("email");
		String password = vo.getString("password");
		// 手机号码正则验证
		if (StringUtils.isNotBlank(memberPhone) && !ValidateUtils.Mobile(memberPhone))
			throw new BizException(1000014, "手机号码");

		// 邮箱正则验证
		if (StringUtils.isNotBlank(orgEmail) && !ValidateUtils.Email(orgEmail))
			throw new BizException(1000014, "企业邮箱");

		// 电话正则验证
		if(StringUtils.isNotBlank(orgPhone)&& !ValidateUtils.Tel(orgPhone))
			throw new BizException(1000014, "电话");
		// 邮箱正则验证
		if (StringUtils.isNotBlank(email) && !ValidateUtils.Email(email))
			throw new BizException(1000014, "用户邮箱");
		if(password.equalsIgnoreCase(vo.getString("rePassword"))){
		}else {
			throw new BizException(1000018);//用户2次输入密码不一致

		}
	}
	//生成企业编码方法
	public String toDistCode(String distCode,Integer existNumber){

		DataVo data=new DataVo();
		data.add("distCode", distCode);
		List<DataVo> dvlist=selectPubOrgByDistCode(data);
		Integer sizeDistCode=0;
		if(dvlist!=null && dvlist.size()>0){
			sizeDistCode=dvlist.size();
		}
		 sizeDistCode=Integer.valueOf(sizeDistCode.toString());
		//有序号
		Integer number=null;
		if(existNumber !=null ){
			number=sizeDistCode+existNumber+1;
		}else if(existNumber==null){
			number=sizeDistCode+1;
		}
		String numStr=number.toString();
		String newNumStr="";
		//不足3位，前面补零
		if(numStr.length()<3){
			StringBuffer sb=new StringBuffer("");
			int zeroSize=3-numStr.length();
			for(int j=0;j<zeroSize;j++){
				sb.append("0");
			}
			sb.append(numStr);
			newNumStr=sb.toString();
		}else{
			newNumStr=numStr;
		}

		String newDistCode=distCode+newNumStr;
		return newDistCode;

	}
}
