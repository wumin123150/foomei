package com.foomei.common.security.shiro;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

public class PasswordHash extends DigestUtils implements InitializingBean {

	private String algorithmName;
	private int hashIterations;

	public void afterPropertiesSet() throws Exception {
		Assert.hasLength(algorithmName, "algorithmName mast be MD5、SHA-1、SHA-256、SHA-384、SHA-512");
	}

	public String toHex(Object source, Object salt) {
		return hashByShiro(algorithmName, source, salt, hashIterations);
	}

	/**
	* 使用shiro的hash方式
	* @param algorithmName 算法
	* @param source 源对象
	* @param salt 加密盐
	* @param hashIterations hash次数
	* @return 加密后的字符
	*/
	public static String hashByShiro(String algorithmName, Object source, Object salt, int hashIterations) {
		return new SimpleHash(algorithmName, source, salt, hashIterations).toHex();
	}

	public static String toHex(byte[] bytes) {
		return Hex.encodeToString(bytes);
	}

	public static byte[] toBytes(String hex) {
		return Hex.decode(hex);
	}

	public static ByteSource toByteSource(String hex) {
		return ByteSource.Util.bytes(toBytes(hex));
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public int getHashIterations() {
		return hashIterations;
	}

	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}
}
