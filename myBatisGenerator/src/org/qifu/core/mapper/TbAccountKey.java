package org.qifu.core.mapper;

public class TbAccountKey {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tb_account.OID
	 * @mbg.generated  Wed Apr 15 10:49:15 CST 2020
	 */
	private String oid;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tb_account.ACCOUNT
	 * @mbg.generated  Wed Apr 15 10:49:15 CST 2020
	 */
	private String account;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tb_account.OID
	 * @return  the value of tb_account.OID
	 * @mbg.generated  Wed Apr 15 10:49:15 CST 2020
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tb_account.OID
	 * @param oid  the value for tb_account.OID
	 * @mbg.generated  Wed Apr 15 10:49:15 CST 2020
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tb_account.ACCOUNT
	 * @return  the value of tb_account.ACCOUNT
	 * @mbg.generated  Wed Apr 15 10:49:15 CST 2020
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tb_account.ACCOUNT
	 * @param account  the value for tb_account.ACCOUNT
	 * @mbg.generated  Wed Apr 15 10:49:15 CST 2020
	 */
	public void setAccount(String account) {
		this.account = account;
	}
}