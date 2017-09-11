package com.clouyun.charge.modules.system.service;

import org.springframework.stereotype.Service;

/**
 * 描述: TODO 
 * 版权: Copyright (c) 2016
 * 公司: 科陆电子
 * 作者: libiao <libiao@szclou.com>
 * 版本: 1.0
 * 创建日期: 2016年12月14日
 */
@Service
public class LockService {

	/*@Autowired
	LockMapper lockMapper;*/

   /* @Autowired
	CacheService cacheService;*/
	
	/**
	 * @description：创建一个全新的登录锁，并保存到缓存库
	 * @author：libiao
	 * @date：2017年1月17日
	 * @param userId
	 * @param lockNo
	 * @return
	 */
	/*public SysLoginLock newLock(Integer userId, String lockNo) {
		//lockMapper.addLock();
		return loginLock;
	}*/
	
	/**
	 * @description：统计登录锁次数
	 * @author：libiao
	 * @date：2017年1月17日
	 * @param loginLock
	 * @return
	 */
	/*public SysLoginLock countLock(SysLoginLock loginLock) {
		//最后时间
		loginLock.setLastTime(new Date());
		//15分钟内连续登陆错误才累计错误次数
		if (!loginLock.isMaxTime()) {
			loginLock.plus1Times();
		}
		return loginLock;
	}*/
	
	/**
	 * @description：获取缓存库中的用户登录锁
	 * @author：libiao
	 * @date：2017年1月17日
	 * @param loginUser
	 * @return
	 */
	/*public SysLoginLock getLock(String lockNo) {
		SysLoginLock loginLock = cacheTemplet.get(lockNo);
		return loginLock;
	}*/
	
	/**
	 * @description：设置登录锁到缓存库
	 * @author：libiao
	 * @date：2017年1月17日
	 * @param loginUser
	 * @param loginLock
	 */
	/*public void setLock(String lockNo, SysLoginLock loginLock) {
		//存入缓存库
		cacheTemplet.set(lockNo, loginLock);
	}*/
	
	/**
	 * @description：清空缓存库中的登录锁
	 * @author：libiao
	 * @date：2017年1月17日
	 * @param lockNo
	 * @return
	 */
	/*public void clearLock(String lockNo) {
		cacheTemplet.set(lockNo, null);
	}*/
	
	/**
	 * @description：解锁用户(数据库,缓存库)
	 * @author：libiao
	 * @date：2016年12月14日
	 * @param loginUser
	 */
	/*public void unlockUser(Integer userId, String lockNo) {
		//解锁用户,状态更新到数据库
		PubUser user = PubUser.newUnlockUser(userId);
		userMapper.updateByPrimaryKey(user);
		//更新登录锁状态,状态更新到緩存库
		setLock(lockNo, null);
	}*/
	
	/**
	 * @description：锁定用户(数据库)
	 * @author：libiao
	 * @date：2016年12月14日
	 * @param loginUser
	 */
	/*public void lockedUser(Integer userId, String lockNo, SysLoginLock loginLock) {
		//锁定用户,状态更新到数据库
		PubUser user = PubUser.newLockedUser(userId);
		userMapper.updateByPrimaryKey(user);
		//更新登录锁状态,状态更新到緩存库
		setLock(lockNo, loginLock);
	}*/
	
}
