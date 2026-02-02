from django.contrib import admin
from .models import Question, Choice

# Register your models here.

class ChoiceInline(admin.StackedInline):
    model = Choice
    extra = 3

class QuestionAdmin(admin.ModelAdmin):
    # fieldsets中每个元组的第一个元素是该字段集的标题。以下是我们的表单现在的样子：
    fieldsets = [
        (None, {"fields": ["question_text"]}),
        ("Date information", {"fields": ["pub_date"]}),
    ]
    inlines = [ChoiceInline]
    # 默认情况下，Django 会显示每个对象的 str()。但有时，如果我们能显示各个字段会更有帮助。要实现这一点，请使用 list_display 管理选项，它是一个字段名元组，会在对象的更改列表页面上以列的形式显示这些字段
    list_display = ["question_text", "pub_date", "was_published_recently"]

    # That adds a “Filter” sidebar that lets people filter the change list by the pub_date field:
    list_filter = ["pub_date"]

    # That adds a search box at the top of the change list. When somebody enters search terms, Django will search the question_text field. You can use as many fields as you’d like – although because it uses a LIKE query behind the scenes, limiting the number of search fields to a reasonable number will make it easier for your database to do the search.
    search_fields = ["question_text"]


# You’ll follow this pattern – create a model admin class, then pass it as the second argument to admin.site.register() – any time you need to change the admin options for a model.
# NOTE(sunyindong.syd): 注册Question模型到admin站点
# 我们需要告诉管理员，Question对象有一个管理界面。要做到这一点，请打开polls/admin.py文件，并将其编辑成如下所示：
admin.site.register(Question, QuestionAdmin)
admin.site.register(Choice)

