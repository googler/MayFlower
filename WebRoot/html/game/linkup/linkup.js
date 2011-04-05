/*--------------------------------------------
 * 连连看 v2.0
 * @author Erhu
 * @since Mar 28th, 2011
 *------------------------------------------*/
var gCanvas;
var gImages;
var gImgCopys = 2;// 每张图片2份拷贝
var gPreGrid;// 前一个选择的格子
var oneResult;// 一个可行解
var imgDir;// 图片文件夹
var helpElem;
var canvasElem;
/**
 * init game
 * @param canvasElem
 * @param rowNums
 * @param colNums
 * @param rowH
 * @param colW
 */
function init(canvasId, helpBtnId, rowNums, colNums, rowH, colW) {
    canvasElem = document.getElementById(canvasId);
    helpElem = document.getElementById(helpBtnId);
    imgDir = "images/";
    gCanvas = new HCanvas(rowNums, colNums, rowH, colW, canvasElem);
    helpElem.onclick = function() {
        // 清除所有选中状态
        for (var i = 0; i < gImages.length; i ++) {
            for (var j = 0; j < gImages[i].length; j ++) {
                gImages[i][j].selected = false;
            }
        }
        drawCanvas();
        if (oneResult != undefined && oneResult.length > 1) {
            reverseImgColor(oneResult[0]);
            reverseImgColor(oneResult[1]);
            setTimeout(drawCanvas, 1000);
        }
    };
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
                images[i].src = imgDir + 'img_' + r_nbrs[j] + '.png';
                i ++;
            }
        }
    }
    // 将图片随机分配给gImages
    gImages = new Array(gCanvas.rowNums);
    for (var m = 0; m < gCanvas.rowNums; m ++) {
        gImages[m] = new Array(gCanvas.colNums);
        for (var n = 0; n < gImages[m].length; n ++) {
            var t_num = Math.floor(Math.random() * images.length);
            gImages[m][n] = new HGrid(m, n, images[t_num]);
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
    canvasElem.addEventListener('click', clickHandler, false);
    this.rowNums = rowNums;
    this.colNums = colNums;
    this.rowH = rowH;
    this.colW = colW;
    this.width = canvasElem.width = colW * colNums + 1;
    this.height = canvasElem.height = rowH * rowNums + 1;
    this.offsetLeft = canvasElem.offsetLeft;
    this.offsetTop = canvasElem.offsetTop;
    this.context = canvasElem.getContext('2d');
    canvasElem.width *= 2;
}

/**
 * 清除画布
 */
HCanvas.prototype.clear = function() {
    this.context.clearRect(0, 0, canvasElem.width, canvasElem.height);
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
    this.context.fillStyle = "#FFF";
    this.context.font = '40px sans-serif';
    this.context.textBaseline = 'top';
    this.context.fillText(txt, 500, 10);
};
/**
 * 绘图片
 */
HCanvas.prototype.drawImages = function(images) {
    var finished = true;
    for (var p = 0; p < gCanvas.rowNums; p ++) {
        for (var q = 0; q < gCanvas.colNums; q ++) {
            if (images[p][q].visible) {
                finished = false;
                this.context.drawImage(images[p][q].img, images[p][q].x(), images[p][q].y(), gCanvas.colW, gCanvas.rowH);
                if (images[p][q].selected)
                    reverseImgColor(images[p][q]);
            }
        }
    }
    if (finished)
        this.drawTxt('You win');
    else {// 检查棋盘是否有解
        for (var i = 0; i < gCanvas.rowNums; i ++) {
            for (var j = 0; j < gCanvas.colNums; j ++) {
                var gridA = images[i][j];
                if (gridA.visible) {
                    var coll = getVisiualImgs(gridA).concat(
                            getVisiualImgsByArr(getUnVisibleImgs(gridA))).concat(
                            getVisiualImgsByArr(getUnVisiualImgsByArr(getUnVisibleImgs(gridA))));
                    for (var k = 0; k < coll.length; k ++) {
                        if (coll[k] != gridA && coll[k].img.src == gridA.img.src) {
                            oneResult = new Array(gridA, coll[k]);
                            return;
                        }
                    }
                }
            }
        }
        this.drawTxt('棋局无解!自动重置...');
        setTimeout(resetImages, 1500);
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
        var t_img_1 = gImages[arrow - 1][column];
        if (t_img_1.visible) {// 图片可见
            coll.push(t_img_1);
            break;
        }
        arrow --;
    }
    // 下
    arrow = row;
    while (1 + arrow < gCanvas.rowNums) {
        var t_img = gImages[arrow + 1][column];
        if (t_img.visible) {
            coll.push(t_img);
            break;
        }
        arrow ++;
    }
    // 左
    arrow = column;
    while (arrow - 1 >= 0) {
        var tt_img = gImages[row][arrow - 1];
        if (tt_img.visible) {
            coll.push(tt_img);
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
 * 重置棋盘
 */
function resetImages() {
    // 取可见的图片
    var visible_img = new Array();
    for (var mm = 0; mm < gImages.length; mm ++) {
        for (var nn = 0; nn < gImages[mm].length; nn ++) {
            if (gImages[mm][nn].visible)
                visible_img.push(gImages[mm][nn].img);
        }
    }
    //  随机分配可见的图片到可见的格子
    for (var m = 0; m < gCanvas.rowNums; m ++) {
        for (var n = 0; n < gImages[m].length; n ++) {
            if (gImages[m][n].visible) {
                var t_num = Math.floor(Math.random() * visible_img.length);
                gImages[m][n].img = visible_img[t_num];
                visible_img = visible_img.slice(0, t_num).concat(visible_img.slice(t_num + 1, visible_img.length));
            }
        }
    }
    drawCanvas();
}
/**
 * 单击事件处理函数
 */
function clickHandler(event) {
    var img = getImgClicked(event);
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
    if (gPreGrid == undefined) {
        gPreGrid = img;
        img.selected = true;
    } else {
        if (img.img.src != gPreGrid.img.src) {// 如果两格子的图片不相同,选中新的格子
            gPreGrid.selected = false;
            img.selected = true;
            gPreGrid = img;
        } else if (img == gPreGrid) {//如果选择了上次选择的格子,则取消上次的选择
            img.selected = false;
            gPreGrid = undefined;
        } else {
            if (!checkImg(gPreGrid, img)) {// 图片相同,但不可消去
                gPreGrid.selected = false;
                img.selected = true;
                gPreGrid = img;
            } else {
                gPreGrid.visible = img.visible = false;
                gPreGrid = undefined;
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
 * 格子
 */
function HGrid(row, col, img) {
    this.row = row;
    this.col = col;
    this.img = img;
    this.visible = true;
    this.selected = false;
}
HGrid.prototype.x = function() {
    return this.col * gCanvas.colW;
};
HGrid.prototype.y = function() {
    return this.row * gCanvas.rowH;
};
/*============================*/
/*----------公共方法----------*/
/*============================*/
/**
 * 反色
 */
function reverseImgColor(grid) {
    var imgd = gCanvas.context.getImageData(grid.x() + 1, grid.y()+ 1, gCanvas.colW - 1, gCanvas.rowH - 1);// +-1除去边线
    var pix = imgd.data;
    for (var i = 0; i < pix.length; i += 4) {
        pix[i] = 255 - pix[i]; // red
        pix[i + 1] = 255 - pix[i + 1]; // green
        pix[i + 2] = 255 - pix[i + 2]; // blue
    }
    gCanvas.context.putImageData(imgd, grid.x() + 1, grid.y() + 1);
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
    var flag = false;
    for (var i = 0; i < arr.length; i ++)
        if (arr[i] == ele)
            flag = true;
    return flag;
}