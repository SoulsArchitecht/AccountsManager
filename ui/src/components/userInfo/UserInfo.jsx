import React, { useState, useEffect } from 'react';
import { useAuth } from '../../authContext/AuthContext';
import { getUserInfo, uploadAvatar } from '../../services/UserService';
import { useForm, Controller } from 'react-hook-form';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { toast } from 'react-toastify';
import './UserInfo.css';
import { useLocalization } from '../../context/LocalizationContext';

const UserInfo = () => {
  const { user, updateUserInfo } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [avatarPreview, setAvatarPreview] = useState(null);
  const { t } = useLocalization();

  const { register, handleSubmit, reset, formState: { errors }, control } = useForm();

  useEffect(() => {
    if (user?.userInfo) {
      reset({
        firstName: user.userInfo.firstName || '',
        lastName: user.userInfo.lastName || '',
        optionalEmail: user.userInfo.optionalEmail || '',
        birthDate: user.userInfo.birthDate ? new Date(user.userInfo.birthDate) : null,
        phone: user.userInfo.phoneNumber || '', 
        country: user.userInfo.country || ''
      });
    }
  }, [user, reset]);

  const handleAvatarChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    if (!file.type.match('image.*')) {
      toast.error('Please select an image file (JPEG, PNG)');
      return;
    }
    if (file.size > 10 * 1024 * 1024) {
      toast.error('File size should be less than 10MB');
      return;
    }

    const reader = new FileReader();
    reader.onloadend = () => setAvatarPreview(reader.result);
    reader.readAsDataURL(file);

    try {
      await uploadAvatar(file);
      toast.success(t('user_info.avatar.success'));
    } catch (error) {
      toast.error(error.response?.data?.message || t('user_info.avatar.error'));
    }
  };

  const onSubmit = async (formData) => {
    try {
      await updateUserInfo({
        ...formData,
        phoneNumber: formData.phone, 
        birthDate: formData.birthDate?.toISOString().split('T')[0]
      });
      toast.success(t('user_info.profile.success'));
      setIsEditing(false);
    } catch (error) {
      toast.error(error.response?.data?.message || t('user_info.profile.error'));
    }
  };

  if (!user || !user.userInfo) return <div className="text-center mt-4">{t('common.loading')}</div>;

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header d-flex justify-content-between align-items-center">
          <h3 className="mb-0">{t('user_info.title')}</h3>
          {!isEditing && (
            <button
              className="btn btn-primary btn-sm"
              onClick={() => setIsEditing(true)}
            >
              {t('user_info.button.edit')}
            </button>
          )}
        </div>
        <div className="card-body">
          <form onSubmit={handleSubmit(onSubmit)}>
            <div className="row mb-4">
              <div className="col-md-3 text-center">
                <div className="mb-3 avatar-container">
                  <img
                    //src={`http://localhost:8080/uploads/${user.userInfo.avatarUrl}`}
                    src={`/uploads/${user.userInfo.avatarUrl}`}
                    alt="Avatar"
                    className="rounded-circle avatar-img"
                  />
                </div>
                {isEditing && (
                  <div className="form-group">
                    <input
                      type="file"
                      id="avatar"
                      accept="image/*"
                      onChange={handleAvatarChange}
                      className="form-control-file d-none"
                    />
                    <label htmlFor="avatar" className="btn btn-outline-secondary btn-sm">
                      {t('user_info.button.change_avatar')}
                    </label>
                  </div>
                )}
              </div>
              <div className="col-md-9">
                <div className="row">
                  <div className="col-md-6">
                    <div className="form-group mb-3">
                      <label className="form-label">{t('table.user_info.first_name')}</label>
                      {isEditing ? (
                        <input
                          {...register('firstName')}
                          className="form-control form-control-sm"
                        />
                      ) : (
                        <div className="form-control-static">{user.userInfo.firstName || '-'}</div>
                      )}
                    </div>
                  </div>
                  <div className="col-md-6">
                    <div className="form-group mb-3">
                      <label className="form-label">{t('table.user_info.last_name')}</label>
                      {isEditing ? (
                        <input
                          {...register('lastName')}
                          className="form-control form-control-sm"
                        />
                      ) : (
                        <div className="form-control-static">{user.userInfo.lastName || '-'}</div>
                      )}
                    </div>
                  </div>
                </div>
                <div className="row">
                  <div className="col-md-6">
                    <div className="form-group mb-3">
                      <label className="form-label">{t('table.user_info.main_email')}</label>
                      <div className="form-control-static text-muted">{user.email}</div>
                    </div>
                  </div>
                  <div className="col-md-6">
                    <div className="form-group mb-3">
                      <label className="form-label">{t('table.user_info.additional_email')}</label>
                      {isEditing ? (
                        <input
                          type="email"
                          {...register('optionalEmail')}
                          className="form-control form-control-sm"
                        />
                      ) : (
                        <div className="form-control-static">{user.userInfo.optionalEmail || '-'}</div>
                      )}
                    </div>
                  </div>
                </div>
                <div className="row">
                  <div className="col-md-6">
                    <div className="form-group mb-3">
                      <label className="form-label">{t('table.user_info.birthdate')}</label>
                      {isEditing ? (
                        <Controller
                          name="birthDate"
                          control={control}
                          render={({ field }) => (
                            <DatePicker
                              selected={field.value}
                              onChange={field.onChange}
                              dateFormat="yyyy-MM-dd"
                              className="form-control form-control-sm"
                              placeholderText="Select birth date"
                              showYearDropdown
                              dropdownMode="select"
                              peekNextMonth
                              showMonthDropdown
                              isClearable
                              withPortal
                            />
                          )}
                        />
                      ) : (
                        <div className="form-control-static">
                          {user.userInfo.birthDate ? new Date(user.userInfo.birthDate).toLocaleDateString() : '-'}
                        </div>
                      )}
                    </div>
                  </div>
                  <div className="col-md-6">
                    <div className="form-group mb-3">
                      <label className="form-label">{t('table.user_info.registration_date')}</label>
                      <div className="form-control-static">
                        {new Date(user.userInfo.registrationDate).toLocaleDateString()}
                      </div>
                    </div>
                  </div>
                </div>
                <div className="row">
                  <div className="col-md-6">
                    <div className="form-group mb-3">
                      <label className="form-label">{t('table.user_info.phone')}</label>
                      {isEditing ? (
                        <input
                          {...register('phone')}
                          className="form-control form-control-sm"
                        />
                      ) : (
                        <div className="form-control-static">{user.userInfo.phoneNumber || '-'}</div>
                      )}
                    </div>
                  </div>
                  <div className="col-md-6">
                    <div className="form-group mb-3">
                      <label className="form-label">{t('table.user_info.country')}</label>
                      {isEditing ? (
                        <input
                          {...register('country')}
                          className="form-control form-control-sm"
                        />
                      ) : (
                        <div className="form-control-static">{user.userInfo.country || '-'}</div>
                      )}
                    </div>
                  </div>
                </div>
                {isEditing && (
                  <div className="d-flex justify-content-end gap-2 mt-3">
                    <button
                      type="button"
                      className="btn btn-secondary btn-sm"
                      onClick={() => {
                        setIsEditing(false);
                        reset();
                      }}
                    >
                      {t('user_info.button.cancel')}
                    </button>
                    <button type="submit" className="btn btn-primary btn-sm">
                      {t('user_info.button.save_changes')}
                    </button>
                  </div>
                )}
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default UserInfo;