import scrapy


class MedicineSpider(scrapy.Spider):
    name = 'medicine'

    start_urls = ['https://www.aptekagemini.pl/category/bez-recepty/10']

    def parse(self, response):
        # follow links to author pages
        for href in response.css('div.name_new a::attr(href)'):
            yield response.follow(href, self.parse_author)

        #follow pagination links
        next_page = response.css('div.next_prev a::attr(href)')[2].extract()
        if next_page is not None:
            yield response.follow(next_page, callback=self.parse)
		
	
    def parse_author(self, response):
        def extract_with_css(query):
            return response.css(query).extract_first().strip()

        yield {
            'name': extract_with_css('h1::text'),
			'price': extract_with_css('div.price span span::text'),
			'description' : extract_with_css('div.pw_description div::text'),
			'image_url' : extract_with_css('div.image_info div a img::attr(src)'),
			'category' : extract_with_css('div.box_link span:nth-child(3) a::text') + '/' + extract_with_css('div.box_link span:nth-child(4) a::text')
			}