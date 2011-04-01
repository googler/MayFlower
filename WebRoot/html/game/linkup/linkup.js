/*--------------------------------------------
 * 连连看 v2.0
 * @author Erhu
 * @since Mar 28th, 2011
 *------------------------------------------*/
var gCanvas;
var gImages;
var gImgCopys = 10;// 每张图片2份拷贝
var gPreImg;// 前一个选择的格子
var oneResult;// 一个可行解
/**
 * init game
 */
function init(canvasElem, timeRemainsElem, rowNums/*总行数*/, colNums/*总列数*/, rowH/*行高*/, colW/*列宽*/) {
    gCanvas = new HCanvas(rowNums, colNums, rowH, colW, canvasElem);
    /*var helpBtn = document.createElement("input");
     helpBtn.id = "linkup_canvas";
     helpBtn.type = 'button';
     helpBtn.value = '提醒';
     helpBtn.onclick = function() {
     // 清除所有选中状态
     for (var i = 0; i < gGrid.length; i ++) {
     for (var j = 0; j < gGrid[i].length; j ++) {
     gGrid[i][j].selected = false;
     }
     }
     drawCanvas();
     if (oneResult != undefined && oneResult.length > 1) {
     reverseImgColor(oneResult[0]);
     reverseImgColor(oneResult[1]);
     setTimeout(drawCanvas, 1000);
     }
     };
     document.body.appendChild(helpBtn);
     */
    initImages();
    drawCanvas();
}

/*
 * init images
 */
function initImages() {
    // 生成所有图片
    var total_img_num = gCanvas.rowNums * gCanvas.colNums;
    var images = new Array(total_img_num);
    var r_nbrs = getRandom(total_img_num / gImgCopys, 0, 60);
    for (var i = 0; i < total_img_num; i ++) {
        for (var j = 0; j < r_nbrs.length; j ++) {
            for (var k = 0; k < gImgCopys; k ++) {
                images[i] = new Image();
                images[i].src = 'images/img_' + r_nbrs[j] + '.png';
                i ++;
            }
        }
    }
    // 将图片随机分配给gImages
    gImages = new Array(gCanvas.rowNums);
    for (var i = 0; i < gCanvas.rowNums; i ++) {
        gImages[i] = new Array(gCanvas.colNums);
        for (var j = 0; j < gImages[i].length; j ++) {
            var t_num = Math.floor(Math.random() * images.length);
            gImages[i][j] = new HImage(i, j, images[t_num]);
            images = images.slice(0, t_num).concat(images.slice(t_num + 1, images.length));
        }
    }
}

/**
 * 绘图
 */
function drawCanvas() {
    gCanvas.clear();
    gCanvas.drawLines();
    gCanvas.drawImages(gImages);
}
/*============================*/
/*----------类定义------------*/
/*============================*/
/**
 * 画布
 * @param rowNums 总行数
 * @param colNums 总列数
 * @param rowH 行高
 * @param colW 列宽
 * @param canvasElem canvasObj
 */
function HCanvas(rowNums, colNums, rowH, colW, canvasElem) {
    if (!canvasElem) {
        canvasElem = document.createElement("canvas");
        canvasElem.id = "canvas";
        canvasElem.addEventListener('click', clickHandler, false);
        document.body.appendChild(canvasElem);
    }
    this.rowNums = rowNums;
    this.colNums = colNums;
    this.rowH = rowH;
    this.colW = colW;
    this.width = canvasElem.width = colW * colNums + 1;
    this.height = canvasElem.height = rowH * rowNums + 1;
    this.offsetLeft = canvasElem.offsetLeft;
    this.offsetTop = canvasElem.offsetTop;
    this.context = canvasElem.getContext('2d');
}

/**
 * 清除画布
 */
HCanvas.prototype.clear = function() {
    this.context.clearRect(0, 0, this.width, this.height);
};

/**
 * 绘网格
 */
HCanvas.prototype.drawLines = function() {
    this.context.beginPath();
    // 画竖线
    for (var x = 0; x <= this.width; x += gCanvas.colW) {
        this.context.moveTo(0.5 + x, 0);
        this.context.lineTo(0.5 + x, this.height);
    }
    // 画横线
    for (var y = 0; y <= this.height; y += gCanvas.rowH) {
        this.context.moveTo(0, 0.5 + y);
        this.context.lineTo(this.width, 0.5 + y);
    }
    // 开画!
    this.context.strokeStyle = "#ccc";
    this.context.stroke();
    this.context.closePath();
};
/**
 * 绘文字
 * @param txt
 */
HCanvas.prototype.drawTxt = function(txt) {
    this.context.strokeStyle = "#F00";
    this.context.font = 'italic 80px sans-serif';
    this.context.textBaseline = 'top';
    this.context.strokeText(txt, 120, 245);
}
/**
 * 绘图片
 */
HCanvas.prototype.drawImages = function(imgs) {
    var finished = true;
    for (var i = 0; i < gCanvas.rowNums; i ++) {
        for (var j = 0; j < gCanvas.colNums; j ++) {
            if (imgs[i][j].visible) {
                finished = false;
                this.context.drawImage(imgs[i][j].img, imgs[i][j].x(), imgs[i][j].y(), gCanvas.colW, gCanvas.rowH);
                if (imgs[i][j].selected)
                    reverseImgColor(imgs[i][j]);
            }
        }
    }
    if (finished)
        this.drawTxt('You win');
    else {// 检查棋盘是否有解
        for (var i = 0; i < gCanvas.rowNums; i ++) {
            for (var j = 0; j < gCanvas.colNums; j ++) {
                var imgA = imgs[i][j];
                if (imgA.visible) {
                    var img = imgA.img;
                    var coll = getVisiualImgs(imgA).concat(
                            getVisiualImgsByArr(getUnVisibleImgs(imgA))).concat(
                            getVisiualImgsByArr(getUnVisiualImgsByArr(getUnVisibleImgs(imgA))));
                    for (var k = 0; k < coll.length; k ++) {
                        if (coll[k].src = img.src) {
                            oneResult = new Array(imgA, coll[k]);
                            return;
                        }
                    }
                }
            }
        }
        this.drawTxt('棋局已死!');
        // setTimeout(resetGrids, 2000);
    }
};
/**
 * 获取某个格子四方可见的格子数组
 */
function getVisiualImgs(img) {
    var coll = new Array();
    var row = img.row;
    var column = img.col;
    // 上
    var arrow = row;// 箭头
    while (arrow - 1 >= 0) {
        var t_img = gImages[arrow - 1][column];
        if (t_img.visible) {// 图片可见
            coll.push(t_img);
            break;
        }
        arrow --;
    }
    // 下
    arrow = row;
    while (1 + arrow < gCanvas.rowNums) {
        var t_grid = gImages[arrow + 1][column];
        if (t_grid.visible) {
            coll.push(t_grid);
            break;
        }
        arrow ++;
    }
    // 左
    arrow = column;
    while (arrow - 1 >= 0) {
        var t_grid = gImages[row][arrow - 1];
        if (t_grid.visible) {
            coll.push(t_grid);
            break;
        }
        arrow --;
    }
    // 右
    arrow = column;
    while (arrow + 1 < gCanvas.colNums) {
        var t_grid = gImages[row][arrow + 1];
        if (t_grid.visible) {
            coll.push(t_grid);
            break;
        }
        arrow ++;
    }
    return coll;
}
function getVisiualImgsByArr(imgArr) {
    var arr = new Array();
    for (var i = 0; i < imgArr.length; i ++) {
        var t_arr = getVisiualImgs(imgArr[i]);
        for (var j = 0; j < t_arr.length; j ++)
            if (!ArrContains(arr, t_arr[j]))
                arr.push(t_arr[j]);
    }
    return arr;
}
/**
 * 取格子四方不可见的格子数组
 */
function getUnVisibleImgs(img) {
    var coll = new Array();
    var row = img.row;
    var column = img.col;
    // 上
    var arrow = row;// 箭头
    while (arrow - 1 >= 0 && !gImages[arrow - 1][column].visible) {
        coll.push(gImages[arrow - 1][column]);
        arrow --;
    }
    // 下
    arrow = row;
    while (1 + arrow < gCanvas.rowNums && !gImages[arrow + 1][column].visible) {
        coll.push(gImages[arrow + 1][column]);
        arrow ++;
    }
    // 左
    arrow = column;
    while (arrow - 1 >= 0 && !gImages[row][arrow - 1].visible) {
        coll.push(gImages[row][arrow - 1]);
        arrow --;
    }
    // 右
    arrow = column;
    while (arrow + 1 < gCanvas.colNums && !gImages[row][arrow + 1].visible) {
        coll.push(gImages[row][arrow + 1]);
        arrow ++;
    }
    return coll;
}
/**
 * 根据不可见方格数组的不可见方格
 */
function getUnVisiualImgsByArr(_grid_arr) {
    var arr = new Array();
    for (var i = 0; i < _grid_arr.length; i ++) {
        var t_arr = getUnVisibleImgs(_grid_arr[i]);
        for (var j = 0; j < t_arr.length; j ++)
            if (!ArrContains(arr, t_arr[j]))
                arr.push(t_arr[j]);
    }
    return arr;
}
/**
 * 单击事件处理函数
 */
function clickHandler(e) {
    var img = getImgClicked(e);
    if (img.visible) {
        checkClick(img);
        oneResult = undefined;
        drawCanvas();
    }
}

/**
 * 获取被点击的图片
 */
function getImgClicked(e) {
    // 物理坐标
    var click_x = e.pageX - gCanvas.offsetLeft;
    var click_y = e.pageY - gCanvas.offsetTop;
    // 行列值
    var column = Math.floor(click_x / gCanvas.colW);
    var row = Math.floor(click_y / gCanvas.rowH);
    return gImages[row][column];
}
/**
 * 检测用户点击行为
 */
function checkClick(img) {
    if (gPreImg == undefined) {
        gPreImg = img;
        img.selected = true;
    } else {
        if (img.src != gPreImg.src) {// 如果两格子的图片不相同,选中新的格子
            gPreImg.selected = false;
            img.selected = true;
            gPreImg = img;
        } else if (img == gPreImg) {//如果选择了上次选择的格子,则取消上次的选择
            img.selected = false;
            gPreImg = undefined;
        } else {
            if (!checkImg(gPreImg, img)) {// 图片相同,但不可消去
                gPreImg.selected = false;
                img.selected = true;
                gPreImg = img;
            } else {
                gPreImg.visible = img.visible = false;
                gPreImg = undefined;
            }
        }
    }
}

/**
 * 计算gridB是否属于gridA的可行解
 * @param imgA
 * @param imgB
 */
function checkImg(imgA, imgB) {
    var flag = false;
    if (ArrContains(getVisiualImgs(imgA), imgB)) {// 不转弯
        flag = true;
    } else {
        var uv = getUnVisibleImgs(imgA);
        if (ArrContains(getVisiualImgsByArr(uv), imgB)) {// 转一个弯
            flag = true;
        } else if (ArrContains(getVisiualImgsByArr(getUnVisiualImgsByArr(uv)), imgB)) {// 转两个弯
            flag = true;
        }
    }
    return flag;
}

/**
 * 图片
 */
function HImage(row, col, img) {
    this.row = row;
    this.col = col;
    this.img = img;
    this.visible = true;
    this.selected = false;
}
HImage.prototype.x = function() {
    return this.col * gCanvas.colW;
};
HImage.prototype.y = function() {
    return this.row * gCanvas.rowH;
};
/*============================*/
/*----------公共方法----------*/
/*============================*/
/**
 * 反色
 */
function reverseImgColor(img) {
    var imgd = gCanvas.context.getImageData(img.x() + 1, img.y() + 1, gCanvas.colW - 1, gCanvas.rowH - 1);// +-1除去边线
    var pix = imgd.data;
    for (var i = 0; i < pix.length; i += 4) {
        pix[i] = 255 - pix[i]; // red
        pix[i + 1] = 255 - pix[i + 1]; // green
        pix[i + 2] = 255 - pix[i + 2]; // blue
    }
    gCanvas.context.putImageData(imgd, img.x() + 1, img.y() + 1);
}

/**
 * 生成nums个不相同的随机整数,上下限分别为low,high-1
 */
function getRandom(nums, low, high) {
    var n = 0;
    var random_nums = new Array(nums);
    while (true) {
        var t_num = Math.floor(Math.random() * (high - low) + low);
        var exist = false;
        for (var i = 0; i < nums; i ++) {
            if (random_nums[i] == undefined) random_nums[i] = low;
            if (random_nums[i] == t_num) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            random_nums[n ++] = t_num;
            if (n >= nums) break;
        }
    }
    return random_nums;
}
function ArrContains(arr, ele) {
    for (var i = 0; i < arr.length; i ++)
        if (arr[i] == ele)
            return true;
}