# BallGameproject
## 原生代码ReycleView 加上surfaceView 实现的打弹珠游戏,无游戏引擎 
###  因为需求比较紧急,所有的东西都根据规则的txt文件PC开发脑补出来的,因为导入游戏资源太大,故自己开发,这个游戏因为要和蓝牙设备配合使用,故增加手势触控,及碰小人的腿便可发射弹珠  
###  所有类似不想用游戏引擎,实现精准碰撞计算,都可用原生解决,效率也不低,仅供参考  
![Image](http://rightinhome.oss-cn-hangzhou.aliyuncs.com/TestObjectFiles/TestObjectFiles/285jgA1BaVJIwsMETYxWpSd7iP6XcNFh/56709279964426783120200515100406.jpg)  
![Image](http://rightinhome.oss-cn-hangzhou.aliyuncs.com/TestObjectFiles/TestObjectFiles/285jgA1BaVJIwsMETYxWpSd7iP6XcNFh/51305929437240563620200515100407.jpg)  
![Image](http://rightinhome.oss-cn-hangzhou.aliyuncs.com/TestObjectFiles/TestObjectFiles/285jgA1BaVJIwsMETYxWpSd7iP6XcNFh/98374828826844835220200515100408.jpg)
  
### 功能 :
#### 1,每关球数固定,在规定的时间内打完所有的目标球,比最后比得分;
#### 2,小球排列相错,大球覆盖在小球之上,此处是用recycleview 实现,就是把item间距变成负的,交错就是每偶数排首位占位数实现,
#### 3,大球是动态添加view 覆盖在上面;
#### 4,动态指针虚线是通过定时器刷新实现;
#### 5,指针的目标点是通过给定数值具体计算;
#### 6,所有的弹窗都是view,显隐都是赋予动画实现;
#### 7,等级管理器来配置参数,更改来决定球的排列,及消失的球;
#### 8,所有目标球被打中,剩余球下落弹起动画是放在surfaceview中,防止UI线程卡顿, 作为对比BallDownLayout就是放在UI线程,2个实现功能完全一样,有兴趣替换下自行尝试;
#### 9,配置文件基本上所有的属性都在里面,改变参数可以改变多有的属性;

## 游戏纯属需求,很多东西仅用来参考,即使不用游戏引擎,体验也不差;
### Tip: 需求比较急,代码很多细节封装没有完善,如果有代码强迫症,请移步本人ZuoMvp(想要迁移至AndroidX的小伙伴要耐心等待,全新MVVM框架正在构建中)
