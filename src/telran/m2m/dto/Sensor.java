package telran.m2m.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sensors")
public class Sensor {
	@Id
	public String time;
	public int id;
	public int dataUBP;
	public int dataLBP;
	public int dataPuls;
	public int dataSugar;

	public Sensor(String time, int id, int dataUBP, int dataLBP, int dataPuls, int dataSugar) {
		super();
		this.time = time;
		this.id = id;
		this.dataUBP = dataUBP;
		this.dataLBP = dataLBP;
		this.dataPuls = dataPuls;
		this.dataSugar = dataSugar;
	}

	public Sensor() {
		super();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDataUBP() {
		return dataUBP;
	}

	public void setDataUBP(int dataUBP) {
		this.dataUBP = dataUBP;
	}

	public int getDataLBP() {
		return dataLBP;
	}

	public void setDataLBP(int dataLBP) {
		this.dataLBP = dataLBP;
	}

	public int getDataPuls() {
		return dataPuls;
	}

	public void setDataPuls(int dataPuls) {
		this.dataPuls = dataPuls;
	}

	public int getDataSugar() {
		return dataSugar;
	}

	public void setDataSugar(int dataSugar) {
		this.dataSugar = dataSugar;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dataLBP;
		result = prime * result + dataPuls;
		result = prime * result + dataSugar;
		result = prime * result + dataUBP;
		result = prime * result + id;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sensor other = (Sensor) obj;
		if (dataLBP != other.dataLBP)
			return false;
		if (dataPuls != other.dataPuls)
			return false;
		if (dataSugar != other.dataSugar)
			return false;
		if (dataUBP != other.dataUBP)
			return false;
		if (id != other.id)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sensor [time=" + time + ", id=" + id + ", dataUBP=" + dataUBP + ", dataLBP=" + dataLBP + ", dataPuls="
				+ dataPuls + ", dataSugar=" + dataSugar + "]";
	}

}
