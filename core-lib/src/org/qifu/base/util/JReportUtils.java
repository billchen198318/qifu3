/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.base.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.properties.JasperreportConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@Component
public class JReportUtils {
	
	private static final String OWNER_PASSWORD = "qifu3";
	
	@Autowired
	DataSource dataSource;
	
	/*
	@Autowired
	@Resource(name = "datasourceOldCoreSystem")
	DataSource datasourceOldCoreSystem;
	*/
	
	@Autowired
	JasperreportConfigProperties jasperreportConfigProperties;
	
	private Connection getConnectionByReportId(String reportId) throws SQLException {
		/*
		if ("PRPCMAIN".equals(reportId)) {
			return dataSource.getConnection();
		}
		if ("CAR001".equals(reportId)) {
			return datasourceOldCoreSystem.getConnection();
		}
		if ("CARTPNMAIN".equals(reportId)) {
			return datasourceOldCoreSystem.getConnection();
		}
		return null;
		*/
		return dataSource.getConnection();
	}	
	
	public void fillReportToResponse(String reportId, Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		if (StringUtils.isBlank(reportId)) {
			throw new java.lang.IllegalArgumentException("error, reportId is blank");
		}
		response.setContentType("application/pdf");
		String jasperFile = jasperreportConfigProperties.getSource() + "/" + reportId + "/" + reportId + ".jasper";
		InputStream reportSource = new FileInputStream( jasperFile );
		this.fillReport(reportId, reportSource, paramMap, response.getOutputStream());
		jasperFile = null;
	}
	
	public void fillReportToByteArray(String reportId, Map<String, Object> paramMap, ByteArrayOutputStream outputStream) throws Exception {
		if (StringUtils.isBlank(reportId)) {
			throw new java.lang.IllegalArgumentException("error, reportId is blank");
		}
		String jasperFile = jasperreportConfigProperties.getSource() + "/" + reportId + "/" + reportId + ".jasper";
		InputStream reportSource = new FileInputStream( jasperFile );
		this.fillReport(reportId, reportSource, paramMap, outputStream);
		jasperFile = null;
	}
	
	private void fillReport(String reportId, InputStream reportSource, Map<String, Object> paramMap, OutputStream outputStream) {
		Connection conn = null;
		try {
			conn = this.getConnectionByReportId(reportId);
		    JasperPrint jasperPrint = JasperFillManager.fillReport(
		            reportSource,
		            paramMap,
		            conn);
		    JRPdfExporter jrPdfExporter=new JRPdfExporter();
		    jrPdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		    jrPdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));		    
		    SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
		    jrPdfExporter.setConfiguration(configuration);
		    configuration.setOwnerPassword( JReportUtils.OWNER_PASSWORD );
		    jrPdfExporter.exportReport();
		    outputStream.flush();
		    outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
			if (null != reportSource) {
				try {
					reportSource.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			reportSource = null;
		}		
		conn = null;
	}
	
}
