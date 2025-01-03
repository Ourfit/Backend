# Generated by Django 4.2 on 2024-12-31 02:08

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Member',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('password', models.CharField(max_length=128, verbose_name='password')),
                ('last_login', models.DateTimeField(blank=True, null=True, verbose_name='last login')),
                ('is_active', models.BooleanField(db_column='is_active', db_comment='멤버 활성화 여부', default=True)),
                ('is_admin', models.BooleanField(db_column='is_admin', db_comment='계정의 어드민 여부', default=False)),
            ],
            options={
                'db_table': 'member',
                'db_table_comment': '멤버 정보 (유저 + 어드민)',
                'ordering': ['-id'],
            },
        ),
        migrations.CreateModel(
            name='MemberProfile',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('member', models.OneToOneField(db_column='member_id', db_comment='연결된 멤버', on_delete=django.db.models.deletion.DO_NOTHING, to='member.member')),
            ],
            options={
                'db_table': 'member_profile',
                'db_table_comment': '멤버의 프로필',
                'ordering': ['-id'],
            },
        ),
    ]
