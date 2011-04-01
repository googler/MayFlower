/*--------------------------------------------
 * 连连看 v1.0
 * @author Erhu
 * @since Mar 26th, 2011
 *------------------------------------------*/
var gCanvas;
var gImages;
var gGrid;gPreGrid
var gRow;
var gColumn;
var gNumOfImg = 2;// 每张图片10份拷贝
var gPreGrid;// 前一个选择的格子
var oneResult;// 一个可行解
/**
 * init game
 */
function init(canvasElement, timeRemainsElement, row, column) {
    gRow = row;
    gColumn = column;
    gCanvas = new HCanvas(gRow, gColumn, canvasElement);
    var helpBtn = document.createElement("input");
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
    initImages();
    initGrids();
    drawCanvas();
}

/*
 * init images
 */
function initImages() {
    var pic_nums = gRow * gColumn / gNumOfImg;
    var r_nbrs = getRandom(pic_nums, 0, 60);
    gImages = new Array(pic_nums);
    for (var i = 0; i < pic_nums; i ++)
        gImages[i] = new HImage('images/img_' + r_nbrs[i] + '.png', gNumOfImg);
}
/**
 * 为格子填充图片
 */
function initGrids() {
    gGrid = new Array(gRow);
    for (var i = 0; i < gRow; i ++) {
        gGrid[i] = new Array(gColumn);
        for (var j = 0; j < gColumn; j ++) {
            while (true) {
                var rdm = Math.floor(Math.random() * (gRow * gColumn / gNumOfImg));// 从源库中取一张图
                if (gImages[rdm].remains > 0) {
                    gGrid[i][j] = new HGrid(i, j, gImages[rdm]);
                    gImages[rdm].remains--;
                    break;
                }
            }
        }
    }
}

/**
 * 绘图
 */
function drawCanvas() {
    gCanvas.clear();
    gCanvas.drawLines();
    gCanvas.drawGrids(gGrid);
}

/**
 * 单击事件处理函数
 */
function clickHandler(e) {
    var grid = getClickedGrid(e);
    if (grid.visible) {
        checkClick(grid);
        oneResult = undefined;
        drawCanvas();
    }
}

/**
 * 检测用户点击行为
 */
function checkClick(grid) {
    if (gPreGrid == undefined) {
        gPreGrid = grid;
        grid.selected = true;
    } else {
        if (grid.hImage != gPreGrid.hImage) {// 如果两格子的图片不相同,选中新的格子
            gPreGrid.selected = false;
            grid.selected = true;
            gPreGrid = grid;
        } else if (grid == gPreGrid) {//如果选择了上次选择的格子,则取消上次的选择
            grid.selected = false;
            gPreGrid = undefined;
        } else {
            if (!checkGrid(gPreGrid, grid)) {// 可以消去
                gPreGrid.selected = false;
                grid.selected = true;
                gPreGrid = grid;
            } else {// 图片相同,但不可消去
                gPreGrid.visible = grid.visible = false;
                gPreGrid = undefined;
            }
        }
    }
}
/**
 * 计算gridB是否属于gridA的可行解
 * @param gridA
 * @param gridB
 */
function checkGrid(gridA, gridB) {
    var flag;
    if (ArrContains(getVisiualGrid(gridA), gridB)/*不转弯*/
            || ArrContains(getVisiualImgByArr(getUnVisibleImg(gridA)), gridB)/*转一个弯*/
            || ArrContains(getVisiualImgByArr(getUnVisiualGridByArr(getUnVisibleImg(gridA))), gridB))/* 取第一次不可见格子的不可见格子的可见格子(转两个弯)*/
        flag = true;
    else
        flag = false;
    return flag;
}
/**
 * 获取某个格子四方可见的格子数组
 */
function getVisiualGrid(grid) {
    var coll = new Array();
    var row = grid.row;
    var column = grid.column;
    // 上
    var arrow = row;// 箭头
    while (arrow - 1 >= 0) {
        var t_grid = gGrid[arrow - 1][column];
        if (t_grid.visible == true) {// 图片可见
            coll.push(t_grid);
            break;
        }
        arrow --;
    }
    // 下
    arrow = row;
    while (1 + arrow < gRow) {
        var t_grid = gGrid[arrow + 1][column];
        if (t_grid.visible == true) {// 图片可见
            coll.push(t_grid);
            break;
        }
        arrow ++;
    }
    // 左
    arrow = column;
    while (arrow - 1 >= 0) {
        var t_grid = gGrid[row][arrow - 1];
        if (t_grid.visible == true) {// 图片可见
            coll.push(t_grid);
            break;
        }
        arrow --;
    }
    // 右
    arrow = column;
    while (arrow + 1 < gColumn) {
        var t_grid = gGrid[row][arrow + 1];
        if (t_grid.visible == true) {// 图片可见
            coll.push(t_grid);
            break;
        }
        arrow ++;
    }
    return coll;
}
function getVisiualImgByArr(_grid_arr) {
    var arr = new Array();
    for (var i = 0; i < _grid_arr.length; i ++) {
        var t_arr = getVisiualGrid(_grid_arr[i]);
        for (var j = 0; j < t_arr.length; j ++) {
            if (!ArrContains(arr, t_arr[j])) {
                arr.push(t_arr[j]);
            }
        }
    }
    return arr;
}
/**
 * 取格子四方不可见的格子数组
 */
function getUnVisibleImg(grid) {
    var coll = new Array();
    var row = grid.row;
    var column = grid.column;
    // 上
    var arrow = row;// 箭头
    while (arrow - 1 >= 0 && gGrid[arrow - 1][column].visible != true) {
        coll.push(gGrid[arrow - 1][column]);
        arrow --;
    }
    // 下
    arrow = row;
    while (1 + arrow < gRow && gGrid[arrow + 1][column].visible != true) {
        coll.push(gGrid[arrow + 1][column]);
        arrow ++;
    }
    // 左
    arrow = column;
    while (arrow - 1 >= 0 && gGrid[row][arrow - 1].visible != true) {
        coll.push(gGrid[row][arrow - 1]);
        arrow --;
    }
    // 右
    arrow = column;
    while (arrow + 1 < gColumn && gGrid[row][arrow + 1].visible != true) {
        coll.push(gGrid[row][arrow + 1]);
        arrow ++;
    }
    return coll;
}
/**
 * 根据不可见方格数组的不可见方格
 */
function getUnVisiualGridByArr(_grid_arr) {
    var arr = new Array();
    for (var i = 0; i < _grid_arr.length; i ++) {
        var t_arr = getUnVisibleImg(_grid_arr[i]);
        for (var j = 0; j < t_arr.length; j ++) {
            if (!ArrContains(arr, t_arr[j])) {
                arr.push(t_arr[j]);
            }
        }
    }
    return arr;
}
/**
 * 获取被点击的格子
 */
function getClickedGrid(e) {
    // 物理坐标
    var click_x = e.pageX - gCanvas.offsetLeft;
    var click_y = e.pageY - gCanvas.offsetTop;
    // 行列值
    var column = Math.floor(click_x / HGrid.width);
    var row = Math.floor(click_y / HGrid.height);
    return gGrid[row][column];
}

/*============================*/
/*----------类定义------------*/
/*============================*/

/**
 * 画布对象
 */
function HCanvas(rownum, columnnum, canvasElem) {
    if (!canvasElem) {
        canvasElem = document.createElement("canvas");
        canvasElem.id = "linkup_canvas";
        canvasElem.addEventListener('click', clickHandler, false);
        document.body.appendChild(canvasElem);
    }
    this.row = rownum;
    this.column = columnnum;
    this.width = canvasElem.width = HGrid.width * columnnum + 1;
    this.height = canvasElem.height = HGrid.height * rownum + 1;
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
    for (var x = 0; x <= this.width; x += HGrid.width) {
        this.context.moveTo(0.5 + x, 0);
        this.context.lineTo(0.5 + x, this.height);
    }    // 画横线
    for (var y = 0; y <= this.height; y += HGrid.height) {
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
HCanvas.prototype.drawGrids = function(grids) {
    var finished = true;
    for (var i = 0; i < this.row; i ++) {
        for (var j = 0; j < this.column; j ++) {
            var t_g = grids[i][j];
            if (t_g.visible) {
                finished = false;
                this.context.drawImage(t_g.hImage.img, t_g.x(), t_g.y(), HGrid.width, HGrid.height);
                if (t_g.selected)
                    reverseImgColor(t_g);
            }
        }
    }
    if (finished)
        this.drawTxt('You win');
    else {// 检查棋盘是否有解
        for (var i = 0; i < grids.length; i ++) {
            for (var j = 0; j < grids[i].length; j ++) {
                var gridA = grids[i][j];
                if (gridA.visible) {
                    var img = gridA.hImage;
                    var coll = getVisiualGrid(gridA).concat(
                            getVisiualImgByArr(getUnVisibleImg(gridA))).concat(
                            getVisiualImgByArr(getUnVisiualGridByArr(getUnVisibleImg(gridA))));
                    for (var k = 0; k < coll.length; k ++) {
                        if (coll[k] != gridA && coll[k].hImage == img) {
                            oneResult = new Array(gridA, coll[k]);
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
 * 重置棋盘
 */
function resetGrids() {
    for (var i = 0; i < gImages.length; i ++)
        gImages[i].remains = gNumOfImg;
    // 获取可见的格子
    var visible_grid = new Array();
    for (var m = 0; m < gGrid.length; m ++) {
        for (var n = 0; n < gGrid[m].length; n ++){
            if (gGrid[m][n].visible)
                visible_grid.push(gGrid[m][n]);
        }
    }
    // 取可见的图片
    var visible_img = new Array();
    for (var m = 0; m < gGrid.length; m ++) {
        for (var n = 0; n < gGrid[m].length; n ++){
            if (gGrid[m][n].visible)
                visible_img.push(gGrid[m][n].hImage);
        }
    }
    //  随机分配可见的图片到可见的格子
    for (var i = 0; i < visible_grid.length; i ++) {

    }
    for (var i = 0; i < gRow; i ++) {
        for (var j = 0; j < gColumn; j ++) {
            if (!gGrid[i][j].visible)
                break;
            while (true) {
                var rdm = Math.floor(Math.random() * (gRow * gColumn / gNumOfImg));// 从源库中取一张图
                if (gImages[rdm].remains > 0) {
                    gGrid[i][j].hImage = gImages[rdm];
                    gImages[rdm].remains--;
                    break;
                }
            }
        }
    }
    drawCanvas();
}
/**
 * 格子对象
 */
function HGrid(row, column, hImage) {
    this.row = row;
    this.column = column;
    this.hImage = hImage;
    this.visible = true;
    this.selected = false;
}
HGrid.width = 48;
HGrid.height = 48;
// 左上角X坐标
HGrid.prototype.x = function() {
    return this.column * HGrid.width;
};
// 左上角Y坐标
HGrid.prototype.y = function() {
    return this.row * HGrid.height;
};
/**
 * 图片
 */
function HImage(path, remains) {
    this.img = new Image();
    this.img.src = path;//图片路径
    this.remains = remains;//剩余张数(分配完毕时为0)
}

/*============================*/
/*----------公共方法----------*/
/*============================*/
/**
 * 反色
 */
function reverseImgColor(grid) {
    var imgd = gCanvas.context.getImageData(grid.x() + 1, grid.y() + 1, HGrid.width - 1, HGrid.height - 1);// +-1除去边线
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
    for (var i = 0; i < arr.length; i ++)
        if (arr[i] == ele)
            return true;
}