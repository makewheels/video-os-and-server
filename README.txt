我想做一个最好用的视频点播

思路是，先在本地转码视频成m3u8，再上传到对象存储，同时对象存储自动预热

再通知服务器

这样服务器可以灵活的

tar zxvf /home/admin/app/package.tgz -C /home/admin/app/
sh /home/admin/app/deploy.sh restart