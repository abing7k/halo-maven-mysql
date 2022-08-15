package run.halo.app.controller.content.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import run.halo.app.model.dto.OptionDTO;
import run.halo.app.model.dto.words.WordRoot;
import run.halo.app.model.properties.CommentProperties;
import run.halo.app.model.support.BaseResponse;
import run.halo.app.service.ClientOptionService;

/**
 * Content option controller.
 *
 * @author johnniang
 * @date 2019-04-03
 */
@RestController("ApiContentOptionController")
@RequestMapping("/api/content/options")
public class OptionController {

    private final ClientOptionService optionService;

    public OptionController(ClientOptionService clientOptionService) {
        this.optionService = clientOptionService;
    }

    @SneakyThrows
    @GetMapping("words")
    @ApiOperation("一言")
    public List<String> getWords() {
        List<String> words = Lists.newArrayList();
       try {
           for (int i = 0; i < 5; i++) {
               HttpResponse<String> send = HttpClient.newHttpClient().send(
                   HttpRequest.newBuilder()
                       .uri(URI.create("https://v1.jinrishici.com/shuqing"))
                       .build(),
                   HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
               words.add(JSON.parseObject(send.body(), WordRoot.class).getContent());
           }
       }catch (Exception e){
           return List.of(
               "不辞山路远，踏雪也相过。",
               "聚散匆匆，此恨年年有。",
               "日日望乡国，空歌白苎词。",
               "雨中禁火空斋冷，江上流莺独坐听。",
               "空自觉、围羞带减，影怯灯孤。",
               "民感桑林雨，云施李靖龙。",
               "屏却相思，近来知道都无益。",
               "人道山长水又断。萧萧微雨闻孤馆。",
               "天长雁影稀，月落山容瘦，冷清清暮秋时候。",
               "不见南师久，谩说北群空。"
           );
       }
        return words;
    }

    @GetMapping("list_view")
    @ApiOperation("Lists all options with list view")
    public List<OptionDTO> listAll() {
        return optionService.listDtos();
    }

    @GetMapping("map_view")
    @ApiOperation("Lists options with map view")
    public Map<String, Object> listAllWithMapView(
        @Deprecated(since = "1.4.8", forRemoval = true)
        @RequestParam(value = "key", required = false) List<String> keyList,
        @RequestParam(value = "keys", required = false) String keys) {
        // handle for key list
        if (!CollectionUtils.isEmpty(keyList)) {
            return optionService.listOptions(keyList);
        }
        // handle for keys
        if (StringUtils.hasText(keys)) {
            var nameSet = Arrays.stream(keys.split(","))
                .map(String::trim)
                .collect(Collectors.toUnmodifiableSet());
            return optionService.listOptions(nameSet);
        }
        // list all
        return optionService.listOptions();
    }

    @GetMapping("keys/{key}")
    @ApiOperation("Gets option value by option key")
    public BaseResponse<Object> getBy(@PathVariable("key") String key) {
        Object optionValue = optionService.getByKey(key).orElse(null);
        return BaseResponse.ok(optionValue);
    }

    @GetMapping("comment")
    @ApiOperation("Options for comment(@deprecated, use /bulk api instead of this.)")
    @Deprecated
    public Map<String, Object> comment() {
        List<String> keys = new ArrayList<>();
        keys.add(CommentProperties.GRAVATAR_DEFAULT.getValue());
        keys.add(CommentProperties.CONTENT_PLACEHOLDER.getValue());
        keys.add(CommentProperties.GRAVATAR_SOURCE.getValue());
        return optionService.listOptions(keys);
    }

}
