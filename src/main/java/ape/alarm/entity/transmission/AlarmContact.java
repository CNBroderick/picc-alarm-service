package ape.alarm.entity.transmission;

import ape.alarm.entity.common.HasComCode;
import ape.master.entity.code.ComCode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bklab.quark.util.json.GsonJsonObjectUtil;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

public class AlarmContact implements HasComCode<AlarmContact> {

    /**
     * 联系人ID
     */
    private int id = -1;
    /**
     * 机构代码。AAAAAAAA 代表适用于全国。
     */
    private ComCode comCode;
    /**
     * 联系人名称
     */
    private String name;
    /**
     * PICC账号
     */
    private String account;
    /**
     * 联络渠道 JSON OBJECT 格式： <br/>
     * { <br/>
     * "邮件": ["user@example.com"], <br/>
     * "手机": ["13012345678", "19920212022"], <br/>
     * "微信": ["wechat_id_1", "wechat_id_2"], <br/>
     * "工单": ["work_sheet_id_1", "work_sheet_id_2"] <br/>
     * } <br/>
     */
    private JsonObject channel = new JsonObject();
    /**
     * 1=启用 0=禁用
     */
    private boolean effective = true;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public AlarmContact() {

    }

    public String getChannelJson() {
        return Optional.ofNullable(channel).map(JsonObject::toString).orElse("{}");
    }

    public List<String> getChannels(AlarmContactChannelTypeEnum channelEnum) {
        return getChannels(channelEnum.name());
    }

    public String getFirstChannel(AlarmContactChannelTypeEnum channelEnum) {
        List<String> channels = getChannels(channelEnum);
        return channels == null || channels.isEmpty() ? null : channels.get(0);
    }

    public List<String> getChannels(String channelTypeName) {
        List<String> list = new ArrayList<>();
        getChannelArray(channelTypeName).forEach(jsonElement -> {
            try {
                list.add(jsonElement.getAsString());
            } catch (Exception ignore) {
            }
        });
        return list;
    }

    public JsonArray getChannelArray(String channelTypeName) {
        JsonObject channel = getChannel();
        try {
            if (!channel.has(channelTypeName) || !channel.get(channelTypeName).isJsonArray()) {
                channel.add(channelTypeName, new JsonArray());
            }
            return channel.getAsJsonArray(channelTypeName);
        } catch (Exception e) {
            JsonArray array = new JsonArray();
            channel.add(channelTypeName, array);
            return array;
        }
    }

    public AlarmContact addChannels(AlarmContactChannelTypeEnum channelType, String... address) {
        JsonArray array = getChannelArray(channelType.name());
        for (String s : address) {
            array.add(s);
        }
        return this;
    }


    public AlarmContact setChannels(AlarmContactChannelTypeEnum channelType, Collection<String> address) {
        channel.add(channelType.name(), address.stream().collect(JsonArray::new, JsonArray::add, (a, b) -> b.forEach(a::add)));
        return this;
    }

    public int getId() {
        return id;
    }

    public AlarmContact setId(int id) {
        this.id = id;
        return this;
    }

    public ComCode getComCode() {
        return comCode;
    }

    public AlarmContact setComCode(ComCode comCode) {
        this.comCode = comCode;
        return this;
    }

    public String getName() {
        return name;
    }

    public AlarmContact setName(String name) {
        this.name = name;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public AlarmContact setAccount(String account) {
        this.account = account;
        return this;
    }

    public JsonObject getChannel() {
        if (channel == null) channel = new JsonObject();
        return channel;
    }

    public AlarmContact setChannel(String channel) {
        try {
            this.channel = new Gson().fromJson(channel, JsonObject.class);
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).info(id + " channel value ");
            this.channel = new JsonObject();
        }
        return this;
    }

    public AlarmContact setChannel(JsonObject channel) {
        this.channel = channel;
        return this;
    }

    public boolean isEffective() {
        return effective;
    }

    public AlarmContact setEffective(boolean effective) {
        this.effective = effective;
        return this;
    }

    public AlarmContact invertEffective() {
        this.effective = !this.effective;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public AlarmContact setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlarmContact that = (AlarmContact) o;
        return id >= 0 && id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new GsonJsonObjectUtil(this).pretty();
    }
}
