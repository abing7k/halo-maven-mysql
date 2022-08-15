/**
 * @Description: 首页
 * @author: 小红
 * @date: 2022/1/6
 * @fileName: by.index
 */

const IndexContext = {
    scrollDown() {
        // noinspection JSCheckFunctionSignatures
        $(".by_scroll_down_effects").click((e) => $("body").animate({scrollTop: $(".by_main_wrap").offset().top}, 700));
    },
    //打字效果
    subtitleTyping(word) {
        word = ThemeConfig.description.length === 0 ? word : ThemeConfig.description
        const subtitle = $(".by_site_subtitle > .subtitle");
        if (!subtitle.length) return;
        new Typed('.by_site_subtitle > .subtitle', {
            // strings: [  ],
            strings: word,
            startDelay: 300,
            typeSpeed: 150,
            loop: true,
            backSpeed: 50,
        });
    },
};

!(function () {
    document.addEventListener("DOMContentLoaded", function () {
        Object.values(IndexContext).forEach(f => $.get("/api/content/options/words", function words(response) {
            f(response.data)
        }));
    });
})();