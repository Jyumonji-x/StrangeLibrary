<template>
    <div id="duplicateForm">
        <el-form :inline="true" :model="duplicateForm" status-icon :rules="rules" ref="duplicateForm" label-width="80px" class="demo-duplicateForm">
            <el-form-item label="馆藏位置" prop="location">
                <el-select v-model="duplicateForm.location">
                    <el-option
                            v-for="item in locations"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="出借状态" prop="status">
                <el-select v-model="duplicateForm.status">
                    <el-option
                            v-for="item in statuses"
                            :key="item.label"
                            :label="item.label"
                            :value="item.label">
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
    export default {
        data: function () {
            const validateLocation = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请选择副本位置'));
                } else {
                    callback();
                }
            };
            const validateStatus = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请选择状态'));
                } else {
                    callback();
                }
            };

            return {
            rules: {
                location: [{validator: validateLocation, trigger: 'blur'}],
                status: [{validator: validateStatus, trigger: 'blur'}],
            },
            duplicateForm: {
                location: '',
                status: ''
            },
            locations: [
                {label: '邯郸'},
                {label: '枫林'},
                {label: '张江'},
                {label: '江湾'}
            ],
            statuses: [
                {label: '可借阅'},
                {label: '已被借阅'},
                {label: '已预约'},
                {label: '已损坏'}
            ]
        };
    }

    }

</script>
