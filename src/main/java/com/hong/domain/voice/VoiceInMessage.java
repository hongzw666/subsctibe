package com.hong.domain.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hong.domain.InMessage;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD) // JAXB从字段获取配置信息
@XmlRootElement(name = "xml") // JAXB读取XML时根元素名称
public class VoiceInMessage extends InMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		@XmlElement(name = "MediaId")
		@JsonProperty("MediaId")
		
		private String mediaId;
		
		@XmlElement(name = "Format")
		@JsonProperty("Format")
		private String format;
		
		@XmlElement(name = "Recognition")
		@JsonProperty("Recognition")
		private String recognition;
		
		public VoiceInMessage() {
			super.setMsgType("voice");
		}
		
		public String getMediaId() {
			return mediaId;
		}

		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getRecognition() {
			return recognition;
		}

		public void setRecognition(String recognition) {
			this.recognition = recognition;
		}

}
